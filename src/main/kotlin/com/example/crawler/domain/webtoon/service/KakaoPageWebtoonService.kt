package com.example.crawler.domain.webtoon.service

import com.example.crawler.domain.webtoon.model.Webtoon
import com.example.crawler.domain.webtoon.model.enums.Platform
import com.example.crawler.domain.webtoon.repository.WebtoonRepository
import com.example.crawler.domain.webtoon.scheduler.WebtoonScheduler
import com.example.crawler.global.infrastructure.KakaoPageWebtoonWebClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class KakaoPageWebtoonService(
    private val webtoonService: WebtoonService,
    private val webClient: KakaoPageWebtoonWebClient,
    private val webtoonRepository: WebtoonRepository
) {
    private val log = LoggerFactory.getLogger(WebtoonScheduler::class.java)
    suspend fun fetchAndSaveAllGenreSections() {
        log.info("장르 섹션 데이터 가져오기 시작")

        val seriesIds = mutableListOf<Long>()
        val batchSize = 100
        val maxConcurrentRequests = 10
        var page = 0
        var stopFetching = false

        val semaphore = Semaphore(maxConcurrentRequests)

        while (!stopFetching) {
            val batchResults = coroutineScope {
                (0 until batchSize).map { offset ->
                    async {
                        semaphore.withPermit {
                            webClient.fetchGenreSection(page + offset, false, "update")
                        }
                    }
                }.awaitAll()
            }

            seriesIds.addAll(batchResults.flatten())
            page += batchSize

            if (batchResults.all { it.isEmpty() }) {
                log.info("마지막 페이지 도달 (page=${page - batchSize}), 루프 종료")
                stopFetching = true
            }

            delay(Random.nextLong(500, 2000))
        }

        log.info("가져온 시리즈 ID 개수: ${seriesIds.size}")
        fetchAllWebtoonDetails(seriesIds)
    }

    suspend fun fetchAllWebtoonDetails(seriesIds: List<Long>) {
        val totalCount = seriesIds.size
        log.info("🚀 총 $totalCount 개의 웹툰 정보를 가져오기 시작")

        val batchSize = 200
        val maxConcurrentRequests = 20
        val semaphore = Semaphore(maxConcurrentRequests)
        val webtoonDetails = mutableListOf<Webtoon>()

        val batches = seriesIds.chunked(batchSize)

        for ((index, batch) in batches.withIndex()) {
            log.info("📦 ${index + 1}번째 배치 요청 (${batch.size}개) 진행 중...")

            val detailResults = coroutineScope {
                batch.map { seriesId ->
                    async {
                        semaphore.withPermit {
                            webClient.fetchWebtoonDetails(seriesId)
                        }
                    }
                }.awaitAll()
            }

            webtoonDetails.addAll(detailResults.filterNotNull())
            delay(Random.nextLong(2000, 4000))

            log.info("✅ ${index + 1}번째 배치 완료! 누적 개수: ${webtoonDetails.size}")
        }

        log.info("🎉 모든 웹툰 정보 수집 완료! 총 ${webtoonDetails.size}개")
        webtoonDetails.chunked(500).forEachIndexed { index, batch ->
            log.info("💾 ${index + 1}번째 배치 저장 (${batch.size}개) 진행 중...")
            webtoonService.saveWebtoons(batch)
            log.info("✅ ${index + 1}번째 배치 저장 완료!")
        }
    }

    suspend fun fetchAndSaveGenreSections() {
        // 신작 처리
        val newReleaseIds = webClient.fetchNewRelease()
        processWebtoonUpdates(newReleaseIds)
        log.info("신작 업데이트 완료")
        // 완결 처리 (페이징)
        var page = 0
        while (true) {
            val completedIds = webClient.fetchGenreSection(page, true, "latest")
            if (completedIds.isEmpty()) break // 더 이상 데이터가 없으면 종료
            val updated = processWebtoonUpdates(completedIds)
            if (!updated) break // 갱신할 것이 없으면 종료
            page++
        }
        log.info("완결 작품 업데이트 완료")
    }

    private suspend fun processWebtoonUpdates(siteWebtoonIds: List<Long>): Boolean {
        var updated = false

        val existingWebtoons = webtoonRepository.findByPlatformAndSiteWebtoonIdIn(Platform.KAKAO_PAGE, siteWebtoonIds)
            .associateBy { it.siteWebtoonId }

        // 웹툰 상세 정보 가져오기
        val newOrUpdatedWebtoons = siteWebtoonIds.mapNotNull { siteWebtoonId ->
            val fetchedWebtoon = webClient.fetchWebtoonDetails(siteWebtoonId) ?: return@mapNotNull null
            val existingWebtoon = existingWebtoons[siteWebtoonId]

            when {
                existingWebtoon == null -> fetchedWebtoon
                existingWebtoon != fetchedWebtoon ->
                    fetchedWebtoon.copy(webtoonId = existingWebtoon.webtoonId)

                else -> null
            }
        }

        // 변경된 웹툰 저장
        if (newOrUpdatedWebtoons.isNotEmpty()) {
            webtoonRepository.saveAll(newOrUpdatedWebtoons)
            updated = true
        }

        return updated
    }
}
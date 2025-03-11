package com.example.crawler.domain.webtoon.service

import com.example.crawler.domain.webtoon.model.Webtoon
import com.example.crawler.global.infrastructure.KakaoPageWebtoonWebClient
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class KakaoPageWebtoonService(
    private val webtoonService: WebtoonService,
    private val webClient: KakaoPageWebtoonWebClient
//    private val failedWebtoonIds: ConcurrentLinkedQueue<Long>
) {
    suspend fun fetchAndSaveGenreSections() {
        println("장르 섹션 데이터 가져오기 시작")

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
                            webClient.fetchGenreSection(page + offset)
                        }
                    }
                }.awaitAll()
            }

            seriesIds.addAll(batchResults.flatten())
            page += batchSize

            if (batchResults.all { it.isEmpty() }) {
                println("마지막 페이지 도달 (page=${page - batchSize}), 루프 종료")
                stopFetching = true
            }

            delay(Random.nextLong(500, 2000))
        }

        println("가져온 시리즈 ID 개수: ${seriesIds.size}")
        fetchAllWebtoonDetails(seriesIds)
    }

    suspend fun fetchAllWebtoonDetails(seriesIds: List<Long>) {
        val totalCount = seriesIds.size
        println("🚀 총 $totalCount 개의 웹툰 정보를 가져오기 시작")

        val batchSize = 200
        val maxConcurrentRequests = 20
        val semaphore = Semaphore(maxConcurrentRequests)
        val webtoonDetails = mutableListOf<Webtoon>()

        val batches = seriesIds.chunked(batchSize)

        for ((index, batch) in batches.withIndex()) {
            println("📦 ${index + 1}번째 배치 요청 (${batch.size}개) 진행 중...")

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

            println("✅ ${index + 1}번째 배치 완료! 누적 개수: ${webtoonDetails.size}")
        }

        println("🎉 모든 웹툰 정보 수집 완료! 총 ${webtoonDetails.size}개")
        webtoonDetails.chunked(500).forEachIndexed { index, batch ->
            println("💾 ${index + 1}번째 배치 저장 (${batch.size}개) 진행 중...")
            webtoonService.saveWebtoons(batch)
            println("✅ ${index + 1}번째 배치 저장 완료!")
        }
    }
}
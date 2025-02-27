package com.example.demo.webtoon.service

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.platforms.kakaopage.service.KakaoPageWebtoonService
import com.example.demo.webtoon.platforms.naver.service.NaverWebtoonService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import kotlin.random.Random

@Service

class AsyncService(
    private val naverWebtoonService: NaverWebtoonService,
    private val kakaoPageWebtoonService: KakaoPageWebtoonService,
    private val commonService: CommonService
) {
    @Async
    fun fetchAndSaveWeekWebtoonsAsync() {
        naverWebtoonService.fetchAndSaveWeekWebtoons()
    }

    @Async
    fun fetchAndSaveDailyWebtoonsAsync() {
        naverWebtoonService.fetchAndSaveDailyPlusWebtoons()
    }

    @Async
    fun fetchAndSaveFinishedWebtoonsAsync() {
        naverWebtoonService.fetchAndSaveFinishedWebtoons()
    }

    @Async("taskExecutor")
    fun fetchGenreSectionAsync(page: Int): CompletableFuture<List<Long>> {
        return CompletableFuture.supplyAsync {
            val responseList = kakaoPageWebtoonService.fetchGenreSection(page)

            val seriesIdList = responseList.flatMap { response ->
                response.data.staticLandingGenreSection.groups.flatMap { group ->
                    group.items.map { it.seriesId }
                }
            }

            println("✅ 최종 반환 리스트: $seriesIdList") // ✅ 결과 리스트 확인
            seriesIdList
        }
    }

    @Async("taskExecutor")
    fun fetchAndSaveGenreSectionsAsync() {
        println("장르 섹션 데이터 가져오기 시작")

        val seriesIds = mutableListOf<Long>()
        val futures = mutableListOf<CompletableFuture<List<Long>>>()
        val batchSize = 100
        var page = 0
        var stopFetching = false

        while (!stopFetching) {
            // 🔥 batchSize만큼 비동기 요청을 동시에 실행
            val batchFutures = (0 until batchSize).map { offset ->
                fetchGenreSectionAsync(page + offset)
            }
            futures.addAll(batchFutures)
            page += batchSize

            // 🔥 현재 배치의 모든 요청 완료될 때까지 대기
            CompletableFuture.allOf(*batchFutures.toTypedArray()).join()

            // 🔥 batch 결과 수집
            val batchResults = batchFutures.map { it.get() }
            val collectedIds = batchResults.flatten()
            seriesIds.addAll(collectedIds)

            // 🔥 종료 조건 확인 (batch 중 하나라도 비어있으면 종료)
            if (batchResults.any { it.isEmpty() }) {
                println("마지막 페이지 도달 (page=${page - batchSize}), 루프 종료")
                stopFetching = true
            }
        }

        // 🔥 모든 요청이 끝날 때까지 대기 (이전 배치들까지 포함)
        CompletableFuture.allOf(*futures.toTypedArray()).join()

        println("가져온 시리즈 ID 개수: ${seriesIds.size}")

        // 시리즈 ID를 이용하여 웹툰 정보 가져오고 영속화
        fetchAllWebtoonDetails(seriesIds)
    }


    @Async("taskExecutor")
    fun fetchWebtoonDetailsAsync(siteWebtoonId: Long): CompletableFuture<Webtoon> {
        return CompletableFuture.supplyAsync {
            kakaoPageWebtoonService.fetchWebtoonDetails(siteWebtoonId)
        }
    }

    @Async("taskExecutor")
    fun fetchAllWebtoonDetails(seriesIds: List<Long>): CompletableFuture<List<Webtoon>> {
        val totalCount = seriesIds.size
        println("🚀 총 $totalCount 개의 웹툰 정보를 가져오기 시작")

        val batchSize = 500
        val webtoonDetails = mutableListOf<Webtoon>()

        val batches = seriesIds.chunked(batchSize)

        for ((index, batch) in batches.withIndex()) {
            println("📦 ${index + 1}번째 배치 요청 (${batch.size}개) 진행 중...")

            val detailFutures = batch.map { seriesId ->
                fetchWebtoonDetailsAsync(seriesId).thenCompose { webtoon ->
                    CompletableFuture.completedFuture(webtoon)
                }
            }

            CompletableFuture.allOf(*detailFutures.toTypedArray()).join()
            webtoonDetails.addAll(detailFutures.map { it.get() })
            Thread.sleep(Random.nextLong(500, 2000))
            println("✅ ${index + 1}번째 배치 완료! 누적 개수: ${webtoonDetails.size}")
        }

        println("🎉 모든 웹툰 정보 수집 완료! 총 ${webtoonDetails.size}개")
        commonService.saveWebtoons(webtoonDetails)

        return CompletableFuture.completedFuture(webtoonDetails)
    }
}
package com.example.demo.webtoon.service

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.platforms.kakaopage.service.KakaoPageWebtoonService
import com.example.demo.webtoon.platforms.naver.service.NaverWebtoonService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicInteger

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
        var page = 0
        val futures = mutableListOf<CompletableFuture<List<Long>>>()

        while (true) {
            val future = fetchGenreSectionAsync(page)
            futures.add(future)
            println(page)
            page++

            // 요청 속도 제한 적용 (10개 요청마다 0.5초 대기)
            if (page % 10 == 0) Thread.sleep(500)

            // 🔥 종료 조건: future가 완료된 후 비어있는지 확인
            future.thenAccept { result ->
                if (result.isEmpty()) {
                    println("마지막 페이지 도달 (page=$page), 루프 종료")
                }
            }

            // 종료 조건이 충족되면 루프 탈출
            if (runCatching { future.get().isEmpty() }.getOrElse { false }) break
        }

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(*futures.toTypedArray()).join()

        // 모든 결과 수집
        val collectedIds = futures.flatMap { it.get() }
        seriesIds.addAll(collectedIds)

        println("가져온 시리즈 ID 개수: ${seriesIds.size}")

        // 시리즈 ID를 이용하여 웹툰 정보 가져오고 영속화까지 진행
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
        val completedCount = AtomicInteger(0) // ✅ 완료된 개수 추적
        val webtoonDetails = mutableListOf<Webtoon>()

        val batches = seriesIds.chunked(batchSize) // ✅ 500개씩 나누기

        for ((index, batch) in batches.withIndex()) {
            println("📦 ${index + 1}번째 배치 요청 (${batch.size}개) 진행 중...")

            val detailFutures = batch.map { seriesId ->
                fetchWebtoonDetailsAsync(seriesId).thenApply { webtoon ->
                    val currentCount = completedCount.incrementAndGet()
                    if (currentCount % 100 == 0 || currentCount == totalCount) {
                        println("✅ 진행 상황: $currentCount / $totalCount (${(currentCount * 100) / totalCount}%) 완료")
                    }
                    webtoon
                }
            }

            CompletableFuture.allOf(*detailFutures.toTypedArray()).join()
            webtoonDetails.addAll(detailFutures.map { it.get() })

            println("✅ ${index + 1}번째 배치 완료! 누적 개수: ${webtoonDetails.size}")
        }

        println("🎉 모든 웹툰 정보 수집 완료! 총 ${webtoonDetails.size}개")
        commonService.saveWebtoons(webtoonDetails)

        return CompletableFuture.completedFuture(webtoonDetails)
    }
}
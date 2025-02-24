package com.example.demo.webtoon.service

import com.example.demo.webtoon.platforms.kakaopage.KakaoPageWebtoonService
import com.example.demo.webtoon.platforms.naver.service.NaverWebtoonService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class AsyncService (
    private val naverWebtoonService: NaverWebtoonService,
    private val kakaoPageWebtoonService: KakaoPageWebtoonService
){
    @Async
    fun fetchAndSaveWeekWebtoonsAsync(){
        naverWebtoonService.fetchAndSaveWeekWebtoons()
    }

    @Async
    fun fetchAndSaveDailyWebtoonsAsync(){
        naverWebtoonService.fetchAndSaveDailyPlusWebtoons()
    }

    @Async
    fun fetchAndSaveFinishedWebtoonsAsync(){
        naverWebtoonService.fetchAndSaveFinishedWebtoons()
    }

    @Async("taskExecutor")
    fun fetchGenreSectionAsync(page: Int): CompletableFuture<List<Long>> {
        return CompletableFuture.supplyAsync {
            val responseList = kakaoPageWebtoonService.fetchGenreSection(page)

            responseList.flatMap { response ->
                response.data.staticLandingGenreSection.groups.flatMap { it.items.map { it.seriesId } }
            }
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

            // 요청 속도 제한 적용 (5개 요청마다 1초 대기)
            if (page % 5 == 0) Thread.sleep(1000)

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

        // ❗ 가져온 데이터를 저장하는 로직 추가 가능 (예: DB 저장)
    }

}
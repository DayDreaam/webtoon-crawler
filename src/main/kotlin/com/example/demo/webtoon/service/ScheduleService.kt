package com.example.demo.webtoon.service

import com.example.demo.webtoon.platforms.kakaopage.service.KakaoPageWebtoonService
import com.example.demo.webtoon.platforms.naver.service.NaverWebtoonService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val naverWebtoonService: NaverWebtoonService,
    private val kakaoPageWebtoonService: KakaoPageWebtoonService,
//    private val failedWebtoonIds: ConcurrentLinkedQueue<Long>
) {
    @Scheduled(cron = "0 0 7 * * *")
    suspend fun scheduledFetchAndSaveWebtoons() {
        println("네이버 웹툰 스케쥴러 실행")
        naverWebtoonService.fetchAndSaveFinishedWebtoons()
        naverWebtoonService.fetchAndSaveWeekWebtoons()
        naverWebtoonService.fetchAndSaveDailyPlusWebtoons()
    }

    @Scheduled(cron = "0 12 14 * * *")
    suspend fun scheduledTask() {
        println("카카오페이지 웹툰 스케쥴러 실행")
        kakaoPageWebtoonService.fetchAndSaveGenreSections()

//        if (failedWebtoonIds.isNotEmpty()) {
//            println("🔄 실패한 ${failedWebtoonIds.size}개 재요청")
//            val retryList = failedWebtoonIds.toList()
//            failedWebtoonIds.clear()
//            kakaoPageWebtoonService.fetchAllWebtoonDetails(retryList)
//        }
    }
}
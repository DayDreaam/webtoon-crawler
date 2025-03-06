package com.example.demo.webtoon.service

import com.example.demo.webtoon.platforms.kakaopage.service.KakaoPageWebtoonService
import com.example.demo.webtoon.platforms.naver.service.NaverWebtoonService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val naverWebtoonService: NaverWebtoonService,
    private val kakaoPageWebtoonService: KakaoPageWebtoonService
) {
    @Scheduled(cron = "0 0 7 * * *")
    suspend fun scheduledFetchAndSaveWebtoons() {
        println("네이버 웹툰 스케쥴러 실행")
        naverWebtoonService.fetchAndSaveFinishedWebtoons()
        naverWebtoonService.fetchAndSaveWeekWebtoons()
        naverWebtoonService.fetchAndSaveDailyPlusWebtoons()
    }

    @Scheduled(cron = "59 37 12 * * *")
    suspend fun scheduledTask() {
        println("카카오페이지 웹툰 스케쥴러 실행")
        kakaoPageWebtoonService.fetchAndSaveGenreSections()
    }
}
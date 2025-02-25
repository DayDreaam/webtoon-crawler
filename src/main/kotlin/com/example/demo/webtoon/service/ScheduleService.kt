package com.example.demo.webtoon.service

import com.example.demo.webtoon.platforms.kakaopage.KakaoPageWebtoonService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val asyncService: AsyncService,
    private val kakaoPageWebtoonService: KakaoPageWebtoonService
) {
    @Scheduled(cron = "20 19 16 * * *")
    fun scheduledFetchAndSaveWebtoons() {
        println("스케쥴러 실행")
        asyncService.fetchAndSaveWeekWebtoonsAsync()
        asyncService.fetchAndSaveDailyWebtoonsAsync()
        asyncService.fetchAndSaveFinishedWebtoonsAsync()
    }

    @Scheduled(cron = "30 39 17 * * *")
    fun scheduledTask() {
        val response = asyncService.fetchAndSaveGenreSectionsAsync()
    }
}
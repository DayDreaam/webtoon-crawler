package com.example.demo.webtoon.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val asyncService: AsyncService
) {
    @Scheduled(cron = "30 31 15 * * *")
    fun scheduledFetchAndSaveWebtoons() {
        println("네이버 웹툰 스케쥴러 실행")
        asyncService.fetchAndSaveWeekWebtoonsAsync()
        asyncService.fetchAndSaveDailyWebtoonsAsync()
        asyncService.fetchAndSaveFinishedWebtoonsAsync()
    }

    @Scheduled(cron = "0 14 17 * * *")
    fun scheduledTask() {
        println("카카오페이지 웹툰 스케쥴러 실행")
        asyncService.fetchAndSaveGenreSectionsAsync()
    }
}
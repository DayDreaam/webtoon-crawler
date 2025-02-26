package com.example.demo.webtoon.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val asyncService: AsyncService
) {
    @Scheduled(cron = "50 11 12 * * *")
    fun scheduledFetchAndSaveWebtoons() {
        println("네이버 웹툰 스케쥴러 실행")
        asyncService.fetchAndSaveWeekWebtoonsAsync()
        asyncService.fetchAndSaveDailyWebtoonsAsync()
        asyncService.fetchAndSaveFinishedWebtoonsAsync()
    }

    @Scheduled(cron = "0 30 12 * * *")
    fun scheduledTask() {
        println("카카오페이지 웹툰 스케쥴러 실행")
        asyncService.fetchAndSaveGenreSectionsAsync()
    }
}
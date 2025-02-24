package com.example.demo.webtoon.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val asyncService: AsyncService
) {
    @Scheduled(cron = "50 33 12 * * *")
    fun scheduledFetchAndSaveWebtoons(){
        println("스케쥴러 실행")
        asyncService.fetchAndSaveWeekWebtoonsAsync()
        asyncService.fetchAndSaveDailyWebtoonsAsync()
        asyncService.fetchAndSaveFinishedWebtoonsAsync()
    }
}
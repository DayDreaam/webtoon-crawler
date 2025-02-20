package com.example.demo.webtoon.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val asyncService: AsyncService
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun scheduledFetchAndSaveWebtoons(){
        println("스케쥴러 실행")
        asyncService.fetchAndSaveWeekWebtoonsAsync()
        println("스케쥴러 종료")
    }
}
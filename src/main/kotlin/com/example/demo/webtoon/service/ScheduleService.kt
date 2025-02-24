package com.example.demo.webtoon.service

import com.example.demo.webtoon.platforms.kakaopage.GraphQLClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduleService(
    private val asyncService: AsyncService,
    private val graphQLClient: GraphQLClient
) {
    @Scheduled(cron = "50 33 12 * * *")
    fun scheduledFetchAndSaveWebtoons(){
        println("스케쥴러 실행")
        asyncService.fetchAndSaveWeekWebtoonsAsync()
        asyncService.fetchAndSaveDailyWebtoonsAsync()
        asyncService.fetchAndSaveFinishedWebtoonsAsync()
    }

    @Scheduled(cron = "50 45 15 * * *")
    fun scheduledTask() {
        val response = graphQLClient.fetchGenreSection(0)
        println("GraphQL Response: $response")
    }
}
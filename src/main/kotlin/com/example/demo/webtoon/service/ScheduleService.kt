package com.example.demo.webtoon.service

import com.example.demo.webtoon.platforms.kakaopage.service.KakaoPageWebtoonService
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

    @Scheduled(cron = "50 11 0 * * *")
    fun scheduledTask() {
        //val response = asyncService.fetchAndSaveGenreSectionsAsync()
        val webtoon = kakaoPageWebtoonService.fetchWebtoonDetails(52484992)
        println(webtoon.webtoonName)
        println(webtoon.platform)
        println(webtoon.siteWebtoonId)
        println(webtoon.webtoonLink)
        println(webtoon.thumbnailUrl)
        println(webtoon.author)
        println(webtoon.finished)
    }
}
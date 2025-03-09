package com.example.crawler.domain.webtoon.service

import com.example.crawler.domain.webtoon.repository.binder.NaverWebtoonMapper
import com.example.crawler.global.infrastructure.NaverWebtoonWebClient
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service

@EnableScheduling
@Service
class NaverWebtoonService(
    private val commonService: CommonService,
    private val webClient: NaverWebtoonWebClient
) {
    suspend fun fetchAndSaveWeekWebtoons() {
        val weekWebtoons = webClient.getWeekWebtoons().values.flatten()
            .map { NaverWebtoonMapper.weekWebtoonToWebtoon(it) }
        commonService.saveWebtoons(weekWebtoons)
        println("✅ 네이버 주간 웹툰 저장 완료")
    }

    suspend fun fetchAndSaveDailyPlusWebtoons() {
        val dailyPlusWebtoons = webClient.getDailyPlusWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        commonService.saveWebtoons(dailyPlusWebtoons)
        println("✅ 네이버 데일리플러스 웹툰 저장 완료")
    }

    suspend fun fetchAndSaveFinishedWebtoons() {
        val finishedWebtoons = webClient.getFinishedWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        commonService.saveWebtoons(finishedWebtoons)
        println("✅ 네이버 완결 웹툰 저장 완료")
    }
}
package com.example.crawler.domain.webtoon.scheduler

import com.example.crawler.domain.webtoon.service.KakaoPageWebtoonService
import com.example.crawler.domain.webtoon.service.NaverWebtoonService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WebtoonScheduler(
    private val naverWebtoonService: NaverWebtoonService,
    private val kakaoPageWebtoonService: KakaoPageWebtoonService
) {
    private val log = LoggerFactory.getLogger(WebtoonScheduler::class.java)

    @Scheduled(cron = "0 0 7 * * *")
    suspend fun scheduledFetchAndSaveWebtoons() {
        log.info("네이버 웹툰 스케쥴러 실행 : {}", LocalDateTime.now().toString())

        naverWebtoonService.fetchAndSaveFinishedWebtoons()
        naverWebtoonService.fetchAndSaveWeekWebtoons()
        naverWebtoonService.fetchAndSaveDailyPlusWebtoons()

        log.info("네이버 웹툰 스케쥴러 실행 완료 : {}", LocalDateTime.now().toString())

    }

    @Scheduled(cron = "0 30 7 * * *")
    suspend fun scheduledTask() {
        log.info("카카오 웹툰 스케쥴러 실행 : {}", LocalDateTime.now().toString())

        kakaoPageWebtoonService.fetchAndSaveGenreSections()

        log.info("카카오 웹툰 스케쥴러 실행 완료 : {}", LocalDateTime.now().toString())
    }
}
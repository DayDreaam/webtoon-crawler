package com.example.crawler.domain.webtoon.service

import com.example.crawler.domain.webtoon.repository.WebtoonRepository
import com.example.crawler.domain.webtoon.repository.binder.NaverWebtoonMapper
import com.example.crawler.global.infrastructure.NaverWebtoonWebClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@EnableScheduling
@Service
class NaverWebtoonService(
    private val webtoonService: WebtoonService,
    private val webClient: NaverWebtoonWebClient,
    private val webtoonRepository: WebtoonRepository
) {
    private val log = LoggerFactory.getLogger(NaverWebtoonService::class.java)

    suspend fun fetchAndSaveWeekWebtoons() {
        val weekWebtoons = webClient.getWeekWebtoons().values.flatten()
            .map { NaverWebtoonMapper.weekWebtoonToWebtoon(it) }
        webtoonService.saveWebtoons(weekWebtoons)
        log.info("✅ 네이버 주간 웹툰 저장 완료")
    }

    suspend fun fetchAndSaveDailyPlusWebtoons() {
        val dailyPlusWebtoons = webClient.getDailyPlusWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        webtoonService.saveWebtoons(dailyPlusWebtoons)
        log.info("✅ 네이버 데일리플러스 웹툰 저장 완료")
    }

    suspend fun fetchAndSaveFinishedWebtoons() {
        val finishedWebtoons = webClient.getFinishedWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        webtoonService.saveWebtoons(finishedWebtoons)
        log.info("✅ 네이버 완결 웹툰 저장 완료")
    }

    suspend fun naverWebtoonInit() {
        log.info("✅ 네이버 웹툰 초기화 시작 {}", LocalDateTime.now().toString())
        fetchAndSaveWeekWebtoons()
        fetchAndSaveDailyPlusWebtoons()
        fetchAndSaveFinishedWebtoons()
        log.info("✅ 네이버 웹툰 초기화 완료 {}", LocalDateTime.now().toString())
    }

    suspend fun fetchAndSaveNewlyReleasedAndFinishedWebtoons() {
        log.info("✅ 네이버 웹툰 업데이트 시작 {}", LocalDateTime.now().toString())

        // ✅ 신작 웹툰 업데이트
        val releasedWebtoon = webClient.getNewlyReleasedWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        webtoonService.saveWebtoons(releasedWebtoon)

        // ✅ 완결 웹툰 업데이트 (페이지별로 확인)
        var page = 1
        while (true) {
            val finishedWebtoons = webClient.getFinishedWebtoonsByPage(page)
                .map { NaverWebtoonMapper.webtoonToWebtoon(it) }

            if (finishedWebtoons.isEmpty()) break

            val platform = finishedWebtoons.first().platform
            val siteWebtoonIds = finishedWebtoons.map { it.siteWebtoonId }

            val existingWebtoons = webtoonRepository.findByPlatformAndSiteWebtoonIdIn(platform, siteWebtoonIds)
                .associateBy { it.siteWebtoonId to it.platform }

            val newOrUpdatedWebtoons = finishedWebtoons.map { webtoon ->
                val existing = existingWebtoons[webtoon.siteWebtoonId to webtoon.platform]
                if (existing == null || existing != webtoon) {
                    webtoon.copy(webtoonId = existing?.webtoonId) // 기존 ID 유지
                } else {
                    null // 변경이 없으면 null
                }
            }.filterNotNull() // null인 항목을 제거

            if (newOrUpdatedWebtoons.isEmpty()) {
                log.info("✅ 완결 웹툰 업데이트 중단: 페이지 {}에서 변경사항 없음", page)
                break
            }
            webtoonService.saveWebtoons(newOrUpdatedWebtoons)
            log.info("✅ {}개의 신규/업데이트된 완결 웹툰 저장 완료 (페이지 {})", newOrUpdatedWebtoons.size, page)
            page++
        }

        log.info("✅ 네이버 웹툰 업데이트 완료 {}", LocalDateTime.now().toString())
    }
}
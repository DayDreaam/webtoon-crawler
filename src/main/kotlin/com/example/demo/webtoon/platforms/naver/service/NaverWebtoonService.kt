package com.example.demo.webtoon.platforms.naver.service

import com.example.demo.webtoon.platforms.naver.dto.*
import com.example.demo.webtoon.platforms.naver.mapper.NaverWebtoonMapper
import com.example.demo.webtoon.service.CommonService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@EnableScheduling
@EnableAsync
@Service
class NaverWebtoonService(
    private val commonService: CommonService,
    private val restTemplate: RestTemplate
) {
    private val objectMapper = jacksonObjectMapper()

    /**
     * 공통 API 요청 함수
     */
    private inline fun <reified T> fetchWebtoonData(url: String): T? {
        val response = restTemplate.getForObject(url, String::class.java) ?: return null
        return objectMapper.readValue(response, T::class.java)
    }

    /**
     * 주간 웹툰 가져오기
     */
    private fun getWeekWebtoons(): Map<String, List<NaverWeekWebtoon>> {
        val url = UriComponentsBuilder
            .fromUri(URI("https://comic.naver.com/api/webtoon/titlelist/weekday"))
            .queryParam("order", "user")
            .build()
            .toUriString()

        return fetchWebtoonData<NaverWeekWebtoonResponse>(url)?.titleListMap ?: emptyMap()
    }

    /**
     * Daily Plus 웹툰 가져오기
     */
    private fun getDailyPlusWebtoons(): List<NaverWebtoon> {
        val url = UriComponentsBuilder
            .fromUri(URI("https://comic.naver.com/api/webtoon/titlelist/weekday"))
            .queryParam("week", "dailyPlus")
            .queryParam("order", "user")
            .build()
            .toUriString()

        return fetchWebtoonData<NaverWebtoonResponse>(url)?.titleList ?: emptyList()
    }

    /**
     * 완결 웹툰 가져오기
     */
    private fun getFinishedWebtoons(): List<NaverWebtoon> {
        val uri = URI("https://comic.naver.com/api/webtoon/titlelist/finished")

        // 첫 번째 페이지 요청
        val firstUrl = UriComponentsBuilder.fromUri(uri).queryParam("page", 1).build().toUriString()
        val firstParsedResponse = fetchWebtoonData<NaverWebtoonPageResponse>(firstUrl) ?: return emptyList()

        val webtoons = firstParsedResponse.titleList.toMutableList()
        val totalPages = firstParsedResponse.pageInfo.totalPages

        // 2페이지부터 반복 요청
        (2..totalPages).forEach { page ->
            val url = UriComponentsBuilder.fromUri(uri).queryParam("page", page).build().toUriString()
            val parsedResponse = fetchWebtoonData<NaverWebtoonPageResponse>(url) ?: return@forEach
            webtoons.addAll(parsedResponse.titleList)
        }

        return webtoons
    }

    /**
     * 각 API에서 데이터 가져와 저장하는 함수들
     */
    fun fetchAndSaveWeekWebtoons() {
        val weekWebtoons = getWeekWebtoons().values.flatten() // 요일별 정보를 제거
            .map { NaverWebtoonMapper.weekWebtoonToWebtoon(it) }
        commonService.saveWebtoons(weekWebtoons)
        println("네이버 주간 웹툰 저장 완료")
    }

    fun fetchAndSaveDailyPlusWebtoons() {
        val dailyPlusWebtoons = getDailyPlusWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        commonService.saveWebtoons(dailyPlusWebtoons)
        println("네이버 데일리플러스 웹툰 저장 완료")
    }

    fun fetchAndSaveFinishedWebtoons() {
        val finishedWebtoons = getFinishedWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        commonService.saveWebtoons(finishedWebtoons)
        println("네이버 완결 웹툰 저장 완료")
    }
}
package com.example.crawler.service

import com.example.crawler.dto.naver.*
import com.example.crawler.mapper.NaverWebtoonMapper
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@EnableScheduling
@Service
class NaverWebtoonService(
    private val commonService: CommonService,
    private val webClient: WebClient
) {
    /**
     * 공통 API 요청 함수
     */
    private suspend inline fun <reified T> fetchWebtoonData(url: String): T? {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(T::class.java) // Mono<T> 반환
            .awaitSingleOrNull() // suspend 함수로 변환
    }

    /**
     * 주간 웹툰 가져오기
     */
    private suspend fun getWeekWebtoons(): Map<String, List<NaverWeekWebtoon>> {
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
    private suspend fun getDailyPlusWebtoons(): List<NaverWebtoon> {
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
    private suspend fun getFinishedWebtoons(): List<NaverWebtoon> {
        val uri = URI("https://comic.naver.com/api/webtoon/titlelist/finished")

        val firstUrl = UriComponentsBuilder.fromUri(uri).queryParam("page", 1).build().toUriString()
        val firstParsedResponse = fetchWebtoonData<NaverWebtoonPageResponse>(firstUrl) ?: return emptyList()

        val webtoons = firstParsedResponse.titleList.toMutableList()
        val totalPages = firstParsedResponse.pageInfo.totalPages

        (2..totalPages).forEach { page ->
            val url = UriComponentsBuilder.fromUri(uri).queryParam("page", page).build().toUriString()
            val parsedResponse = fetchWebtoonData<NaverWebtoonPageResponse>(url)
            if (parsedResponse != null) {
                webtoons.addAll(parsedResponse.titleList)
            } else {
                println("⚠️ 페이지 $page 데이터 가져오기 실패")
            }
        }

        return webtoons
    }

    /**
     * 각 API에서 데이터 가져와 저장하는 함수
     */
    suspend fun fetchAndSaveWeekWebtoons() {
        val weekWebtoons = getWeekWebtoons().values.flatten()
            .map { NaverWebtoonMapper.weekWebtoonToWebtoon(it) }
        commonService.saveWebtoons(weekWebtoons)
        println("✅ 네이버 주간 웹툰 저장 완료")
    }

    suspend fun fetchAndSaveDailyPlusWebtoons() {
        val dailyPlusWebtoons = getDailyPlusWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        commonService.saveWebtoons(dailyPlusWebtoons)
        println("✅ 네이버 데일리플러스 웹툰 저장 완료")
    }

    suspend fun fetchAndSaveFinishedWebtoons() {
        val finishedWebtoons = getFinishedWebtoons()
            .map { NaverWebtoonMapper.webtoonToWebtoon(it) }
        commonService.saveWebtoons(finishedWebtoons)
        println("✅ 네이버 완결 웹툰 저장 완료")
    }
}
package com.example.crawler.global.infrastructure

import com.example.crawler.global.infrastructure.dto.naver.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class NaverWebtoonWebClient(
    private val naverWebtoonClient: WebClient
) {

    companion object {
        @Value("\${site-uri.naver.weekday}")
        private lateinit var WEEKDAY_URI: String;

        @Value("\${site-uri.naver.finished}")
        private lateinit var FINISHED_URI: String;
    }


    /**
     * 공통 API 요청 함수
     */
    private suspend inline fun <reified T> fetchWebtoonData(url: String): T? {
        return naverWebtoonClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(T::class.java) // Mono<T> 반환
            .awaitSingleOrNull() // suspend 함수로 변환
    }

    /**
     * 주간 웹툰 가져오기
     */
    suspend fun getWeekWebtoons(): Map<String, List<NaverWeekWebtoon>> {
        val url = UriComponentsBuilder
            .fromUri(URI(WEEKDAY_URI))
            .queryParam("order", "user")
            .build()
            .toUriString()

        return fetchWebtoonData<NaverWeekWebtoonResponse>(url)?.titleListMap ?: emptyMap()
    }

    /**
     * Daily Plus 웹툰 가져오기
     */
    suspend fun getDailyPlusWebtoons(): List<NaverWebtoon> {
        val url = UriComponentsBuilder
            .fromUri(URI(WEEKDAY_URI))
            .queryParam("week", "dailyPlus")
            .queryParam("order", "user")
            .build()
            .toUriString()

        return fetchWebtoonData<NaverWebtoonResponse>(url)?.titleList ?: emptyList()
    }

    /**
     * 완결 웹툰 가져오기
     */
    suspend fun getFinishedWebtoons(): List<NaverWebtoon> {
        val uri = URI(FINISHED_URI)

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
}
package com.example.crawler.global.infrastructure

import com.example.crawler.global.infrastructure.dto.naver.*
import com.example.crawler.global.infrastructure.property.NaverWebtoonProperties
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class NaverWebtoonWebClient(
    private val naverWebtoonClient: WebClient,
    private val properties: NaverWebtoonProperties
) {
    val WEEKDAY_URI: String get() = properties.weekday
    val FINISHED_URI: String get() = properties.finished
    val NEW_URI: String get() = properties.new

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
     * 완결 웹툰 전부 가져오기
     */
    suspend fun getFinishedWebtoons(): List<NaverWebtoon> {
        val uri = URI(FINISHED_URI)

        val firstUrl = UriComponentsBuilder.fromUri(uri).queryParam("page", 1).build().toUriString()
        val firstParsedResponse = fetchWebtoonData<NaverWebtoonPageResponse>(firstUrl) ?: return emptyList()

        val webtoons = firstParsedResponse.titleList.toMutableList()
        val totalPages = firstParsedResponse.pageInfo.totalPages

        (2..totalPages).forEach { page ->
            val pageWebtoons = getFinishedWebtoonsByPage(page)
            if (pageWebtoons.isNotEmpty()) {
                webtoons.addAll(pageWebtoons)
            } else {
                println("⚠️ 페이지 $page 데이터 가져오기 실패")
            }
        }

        return webtoons
    }

    /**
     * 특정 페이지의 완결 웹툰 가져오기
     */
    suspend fun getFinishedWebtoonsByPage(page: Int): List<NaverWebtoon> {
        val url = UriComponentsBuilder.fromUri(URI(FINISHED_URI))
            .queryParam("page", page)
            .build()
            .toUriString()

        return fetchWebtoonData<NaverWebtoonPageResponse>(url)?.titleList ?: emptyList()
    }

    /**
     * 신작 웹툰 가져오기
     */
    suspend fun getNewlyReleasedWebtoons(): List<NaverWebtoon> {
        val url = UriComponentsBuilder
            .fromUri(URI(NEW_URI))
            .queryParam("order", "update")
            .build()
            .toUriString()

        return fetchWebtoonData<NaverWebtoonResponse>(url)?.titleList ?: emptyList()
    }
}
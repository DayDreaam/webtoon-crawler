package com.example.demo.webtoon.naver.service

import com.example.demo.webtoon.naver.dto.NaverWeekWebtoon
import com.example.demo.webtoon.naver.dto.NaverWeekWebtoonResponse
import com.example.demo.webtoon.naver.mapper.NaverWebtoonMapper
import com.example.demo.webtoon.repository.WebtoonRepository
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
class NaverWebtoonService (
    private val webtoonRepository: WebtoonRepository
) {

    private val restTemplate = RestTemplate()
    private val objectMapper = jacksonObjectMapper()

    private fun getWeekWebtoons(): Map<String, List<NaverWeekWebtoon>> {
        val uri = URI("https://comic.naver.com/api/webtoon/titlelist/weekday")

        val url = UriComponentsBuilder
            .fromUri(uri)
            .queryParam("order", "user")
            .build()
            .toUriString()

        val response = restTemplate.getForObject(url, String::class.java) ?: return emptyMap()
        return parseWeekWebtoonJson(response).titleListMap
    }

    private fun parseWeekWebtoonJson(json: String): NaverWeekWebtoonResponse {
        return objectMapper.readValue(json, NaverWeekWebtoonResponse::class.java)
    }

    private fun saveWeekWebtoon(titleListMap: Map<String, List<NaverWeekWebtoon>>) {
        val allWebtoonNames = titleListMap.values.flatten().map { it.titleName }
        val existingWebtoons = webtoonRepository.findByWebtoonNameIn(allWebtoonNames)
        val existingWebtoonMap = existingWebtoons.associateBy { it.webtoonName }

        val newOrUpdatedWebtoons = titleListMap.values.flatten().mapNotNull { webtoon ->
            val existing = existingWebtoonMap[webtoon.titleName]
            val newWebtoon = NaverWebtoonMapper.weekWebtoonToWebtoon(webtoon)
            if (existing == null || existing != newWebtoon) newWebtoon else null
        }

        if (newOrUpdatedWebtoons.isNotEmpty()) {
            newOrUpdatedWebtoons.chunked(100).forEach { batch ->
                webtoonRepository.saveAll(batch)
            }
        }
    }

    fun fetchAndSaveWeekWebtoons() {
        val titleListMap = getWeekWebtoons()
        saveWeekWebtoon(titleListMap)
    }


}
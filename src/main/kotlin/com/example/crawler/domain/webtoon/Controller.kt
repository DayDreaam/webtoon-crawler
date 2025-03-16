package com.example.crawler.domain.webtoon

import com.example.crawler.global.infrastructure.KakaoPageWebtoonWebClient
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val kakaoPageWebtoonWebClient: KakaoPageWebtoonWebClient
) {
    @GetMapping("/newRelease")
    fun test(): ResponseEntity<List<Long>> {
        val seriesIds = runBlocking { kakaoPageWebtoonWebClient.fetchNewRelease() }
        return ResponseEntity.ok(seriesIds)
    }
}
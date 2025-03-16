package com.example.crawler.domain.webtoon

import com.example.crawler.domain.webtoon.service.KakaoPageWebtoonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val kakaoPageWebtoonService: KakaoPageWebtoonService
) {
    @GetMapping("/fetch-genre-sections")
    suspend fun fetchAndSaveGenreSections(): ResponseEntity<String> {
        kakaoPageWebtoonService.fetchAndSaveGenreSections()
        return ResponseEntity.ok("장르 섹션 데이터 갱신 완료")
    }
}
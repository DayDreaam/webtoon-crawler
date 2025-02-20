package com.example.demo.controller

import com.example.demo.service.NaverWebtoonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/webtoons")
class WebtoonController(private val naverWebtoonService: NaverWebtoonService) {

    @GetMapping("/naver/save")
    fun saveWebtoons(): String {
        naverWebtoonService.fetchAndSaveWeekWebtoons()
        return "네이버 웹툰 데이터를 저장했습니다!"
    }
}
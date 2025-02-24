package com.example.demo.webtoon.platforms.naver.dto

data class NaverWeekWebtoonResponse(
    val titleListMap: Map<String, List<NaverWeekWebtoon>>,
    val dayOfWeek: String
)
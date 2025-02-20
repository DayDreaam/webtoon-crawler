package com.example.demo.webtoon.naver.dto

data class NaverWeekWebtoonResponse(
    val titleListMap: Map<String, List<NaverWeekWebtoon>>,
    val dayOfWeek: String
)
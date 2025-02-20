package com.example.demo.dto

data class NaverWeekWebtoonResponse(
    val titleListMap: Map<String, List<NaverWeekWebtoon>>,
    val dayOfWeek: String
)
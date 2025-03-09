package com.example.crawler.global.infrastructure.dto.naver

data class NaverWeekWebtoonResponse(
    val titleListMap: Map<String, List<NaverWeekWebtoon>>,
    val dayOfWeek: String
)
package com.example.crawler.dto.naver

data class NaverWeekWebtoonResponse(
    val titleListMap: Map<String, List<NaverWeekWebtoon>>,
    val dayOfWeek: String
)
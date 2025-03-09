package com.example.crawler.global.infrastructure.dto.naver

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverWeekWebtoon(
    val titleId: Long,
    val titleName: String,
    val author: String,
    val thumbnailUrl: String,
    val finish: Boolean,
)
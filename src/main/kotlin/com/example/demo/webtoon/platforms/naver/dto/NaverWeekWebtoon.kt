package com.example.demo.webtoon.platforms.naver.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverWeekWebtoon(
    val titleId: Long,
    val titleName: String,
    val author: String,
    val thumbnailUrl: String,
    val finish: Boolean,
)
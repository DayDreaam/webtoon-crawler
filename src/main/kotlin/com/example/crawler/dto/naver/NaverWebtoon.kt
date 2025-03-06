package com.example.crawler.dto.naver

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverWebtoon(
    val titleId: Long,
    val titleName: String,
    val author: String,
    val thumbnailUrl: String,
    val finish: Boolean
)
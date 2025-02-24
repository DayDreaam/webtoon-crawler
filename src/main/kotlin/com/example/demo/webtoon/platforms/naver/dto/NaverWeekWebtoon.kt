package com.example.demo.webtoon.platforms.naver.dto

data class NaverWeekWebtoon(
    val titleId: Long,
    val titleName: String,
    val author: String,
    val thumbnailUrl: String,
    val up: Boolean,
    val rest: Boolean,
    val bm: Boolean,
    val adult: Boolean,
    val starScore: Double,
    val viewCount: Long,
    val openToday: Boolean,
    val potenUp: Boolean,
    val bestChallengeLevelUp: Boolean,
    val finish: Boolean,
    val new: Boolean
)
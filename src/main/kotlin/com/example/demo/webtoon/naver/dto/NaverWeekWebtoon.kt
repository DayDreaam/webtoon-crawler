package com.example.demo.webtoon.naver.dto

data class NaverWeekWebtoon(
    val titleId: Int,
    val titleName: String,
    val author: String,
    val thumbnailUrl: String,
    val up: Boolean,
    val rest: Boolean,
    val bm: Boolean,
    val adult: Boolean,
    val starScore: Double,
    val viewCount: Int,
    val openToday: Boolean,
    val potenUp: Boolean,
    val bestChallengeLevelUp: Boolean,
    val finish: Boolean,
    val new: Boolean
)
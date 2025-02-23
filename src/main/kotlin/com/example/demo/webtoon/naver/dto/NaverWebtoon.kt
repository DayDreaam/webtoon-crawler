package com.example.demo.webtoon.naver.dto

data class NaverWebtoon(
    val titleId: Int,
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
    val new: Boolean,
    val novelOriginAuthors: List<IdName>,
    val writers: List<IdName>,
    val painters: List<IdName>,
)
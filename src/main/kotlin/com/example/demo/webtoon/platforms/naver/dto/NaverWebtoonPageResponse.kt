package com.example.demo.webtoon.platforms.naver.dto

data class NaverWebtoonPageInfo (
    val endRowNum :Int,
    val firstPage :Int,
    val indexSize :Int,
    val lastPage :Int,
    val nextPage :Int,
    val page :Int,
    val pageSize :Int,
    val prevPage :Int,
    val rawPage :Int,
    val startRowNum :Int,
    val totalPages :Int,
    val totalRows : Int
)

data class NaverWebtoonPageResponse(
    val pageInfo : NaverWebtoonPageInfo,
    val titleList :List<NaverWebtoon>
)
package com.example.crawler.mapper

import com.example.crawler.dto.kakaopage.WebtoonContent
import com.example.crawler.entity.Webtoon
import com.example.crawler.entity.enums.Platform

object KakaoPageWebtoonMapper {
    fun webtoonContentToWebtoon(siteWebtoonId: Long, content: WebtoonContent): Webtoon {
        val convertedWebtoon = Webtoon(
            webtoonName = content.title,
            platform = Platform.KAKAO_PAGE,
            siteWebtoonId = siteWebtoonId,
            webtoonLink = "https://page.kakao.com/content/$siteWebtoonId",
            thumbnailUrl = content.thumbnail,
            authors = content.authors,
            finished = content.onIssue == "End"
        )
        return convertedWebtoon
    }
}
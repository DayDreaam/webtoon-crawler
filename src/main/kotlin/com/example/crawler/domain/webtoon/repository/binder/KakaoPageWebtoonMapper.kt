package com.example.crawler.domain.webtoon.repository.binder

import com.example.crawler.domain.webtoon.model.Webtoon
import com.example.crawler.domain.webtoon.model.enums.Platform
import com.example.crawler.global.infrastructure.dto.kakaopage.WebtoonContent

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
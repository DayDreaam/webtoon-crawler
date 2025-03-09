package com.example.crawler.domain.webtoon.repository.binder

import com.example.crawler.domain.webtoon.model.Webtoon
import com.example.crawler.domain.webtoon.model.enums.Platform
import com.example.crawler.global.infrastructure.dto.naver.NaverWebtoon
import com.example.crawler.global.infrastructure.dto.naver.NaverWeekWebtoon

object NaverWebtoonMapper {
    fun weekWebtoonToWebtoon(webtoon: NaverWeekWebtoon): Webtoon {
        val convertedWebtoon = Webtoon(
            webtoonName = webtoon.titleName,
            siteWebtoonId = webtoon.titleId,
            webtoonLink = "https://comic.naver.com/webtoon/list?titleId=${webtoon.titleId}",
            platform = Platform.NAVER_WEBTOON,
            thumbnailUrl = webtoon.thumbnailUrl,
            authors = webtoon.author,
            finished = webtoon.finish
        )
        return convertedWebtoon
    }

    fun webtoonToWebtoon(webtoon: NaverWebtoon): Webtoon {
        val convertedWebtoon = Webtoon(
            webtoonName = webtoon.titleName,
            siteWebtoonId = webtoon.titleId,
            webtoonLink = "https://comic.naver.com/webtoon/list?titleId=${webtoon.titleId}",
            platform = Platform.NAVER_WEBTOON,
            thumbnailUrl = webtoon.thumbnailUrl,
            authors = webtoon.author,
            finished = webtoon.finish
        )
        return convertedWebtoon
    }
}
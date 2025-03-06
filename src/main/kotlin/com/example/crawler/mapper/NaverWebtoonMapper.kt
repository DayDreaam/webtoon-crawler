package com.example.crawler.mapper

import com.example.crawler.dto.naver.NaverWebtoon
import com.example.crawler.dto.naver.NaverWeekWebtoon
import com.example.crawler.entity.Webtoon
import com.example.crawler.entity.enums.Platform

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
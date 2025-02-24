package com.example.demo.webtoon.naver.mapper

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.enums.Platform
import com.example.demo.webtoon.naver.dto.NaverWebtoon
import com.example.demo.webtoon.naver.dto.NaverWeekWebtoon

object NaverWebtoonMapper {
    fun weekWebtoonToWebtoon(webtoon: NaverWeekWebtoon) : Webtoon {
        val convertedWebtoon = Webtoon(
            webtoonName = webtoon.titleName,
            siteWebtoonId = webtoon.titleId,
            webtoonLink = "https://comic.naver.com/webtoon/list?titleId=${webtoon.titleId}",
            platform = Platform.NAVER_WEBTOON,
            thumbnailUrl = webtoon.thumbnailUrl,
            author = webtoon.author,
            finished = webtoon.finish
        )
        return convertedWebtoon
    }

    fun webtoonToWebtoon(webtoon: NaverWebtoon) : Webtoon {
        val convertedWebtoon = Webtoon(
            webtoonName = webtoon.titleName,
            siteWebtoonId = webtoon.titleId,
            webtoonLink = "https://comic.naver.com/webtoon/list?titleId=${webtoon.titleId}",
            platform = Platform.NAVER_WEBTOON,
            thumbnailUrl = webtoon.thumbnailUrl,
            author = webtoon.author,
            finished = webtoon.finish
        )
        return convertedWebtoon
    }
}
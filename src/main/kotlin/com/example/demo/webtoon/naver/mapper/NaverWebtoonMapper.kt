package com.example.demo.webtoon.naver.mapper

import com.example.demo.webtoon.naver.dto.NaverWeekWebtoon
import com.example.demo.webtoon.entity.Webtoon

object NaverWebtoonMapper {
    fun weekWebtoonToWebtoon(webtoon: NaverWeekWebtoon) : Webtoon {
        val newWebtoon = Webtoon(
            webtoonName = webtoon.titleName,
            webtoonLink = "https://comic.naver.com/webtoon/list?titleId=${webtoon.titleId}",
            platform = "NAVER_WEBTOON",
            thumbnailUrl = webtoon.thumbnailUrl,
            author = webtoon.author,
            finished = webtoon.finish
        )
        return newWebtoon
    }
}
package com.example.demo.mapper

import com.example.demo.dto.NaverWeekWebtoon
import com.example.demo.entity.Webtoon

object NaverWebtoonMapper {
    fun weekWebtoonToWebtoon(webtoon: NaverWeekWebtoon) :Webtoon {
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
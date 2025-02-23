package com.example.demo.webtoon.service

import com.example.demo.webtoon.naver.service.NaverWebtoonService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class AsyncService (
    private val naverWebtoonService: NaverWebtoonService
){
    @Async
    fun fetchAndSaveWeekWebtoonsAsync(){
        naverWebtoonService.fetchAndSaveWeekWebtoons()
    }

    @Async
    fun fetchAndSaveDailyWebtoonsAsync(){
        naverWebtoonService.fetchAndSaveDailyPlusWebtoons()
    }

    @Async
    fun fetchAndSaveFinishedWebtoonsAsync(){
        naverWebtoonService.fetchAndSaveFinishedWebtoons()
    }
}
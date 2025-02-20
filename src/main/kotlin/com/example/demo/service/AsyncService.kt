package com.example.demo.service

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
}
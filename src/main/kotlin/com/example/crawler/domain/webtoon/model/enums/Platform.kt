package com.example.crawler.domain.webtoon.model.enums

import org.slf4j.LoggerFactory

enum class Platform(val platformName: String) {
    NAVER_WEBTOON("NAVER"),
    KAKAO_PAGE("KAKAO_PAGE");

    companion object {
        private val log = LoggerFactory.getLogger(Platform::class.java)

        fun fromString(value: String): Platform {
            return entries.find { it.platformName.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("No enum constant ${Platform::class.qualifiedName}.$value")
        }
    }
}

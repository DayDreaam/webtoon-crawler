package com.example.crawler.global.infrastructure.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "site-url")
class KakaoPageWebtoonProperties {
    lateinit var kakao: String
}
package com.example.crawler.global.infrastructure.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "site-url.naver")
class NaverWebtoonProperties {
    lateinit var weekday: String
    lateinit var finished: String
    lateinit var new: String
}
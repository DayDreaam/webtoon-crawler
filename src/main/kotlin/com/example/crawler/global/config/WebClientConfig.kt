package com.example.crawler.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    
    @Bean
    fun naverWebtoonClient(): WebClient {
        return WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs { configure -> configure.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) }
                    .build())
            .build()
    }

    @Bean
    fun kakaoWebtoonClient(): WebClient {
        return WebClient.builder()
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs { configure -> configure.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) }
                    .build())
            .build()
    }

    // readTimeout ...
    // 스레드 풀 ...
}
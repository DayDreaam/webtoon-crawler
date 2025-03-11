package com.example.crawler.domain.webtoon.init

import com.example.crawler.domain.webtoon.repository.WebtoonRepository
import com.example.crawler.domain.webtoon.service.NaverWebtoonService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class WebtoonInitializer(
    private val webtoonRepository: WebtoonRepository,
    private val naverWebtoonService: NaverWebtoonService
) : ApplicationRunner {
    private val log = LoggerFactory.getLogger(WebtoonInitializer::class.java)
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun run(args: ApplicationArguments?) {
        if (webtoonRepository.count() == 0L) {
            log.info("저장된 웹툰이 없습니다. 전체 데이터 받아오기를 실행합니다.")
            scope.launch {
                naverWebtoonService.naverWebtoonInit()
            }
        } else {
            log.info("웹툰 데이터가 이미 존재합니다. 스케쥴러로 업데이트 합니다.")
        }
    }
}

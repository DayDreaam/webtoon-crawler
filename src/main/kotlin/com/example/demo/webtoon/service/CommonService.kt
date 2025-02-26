package com.example.demo.webtoon.service

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.enums.Platform
import com.example.demo.webtoon.repository.WebtoonRepository
import org.springframework.stereotype.Service

@Service
class CommonService(
    private val webtoonRepository: WebtoonRepository
) {
    fun saveWebtoons(webtoons: List<Webtoon>) {
        val platform: Platform = webtoons.first().platform
        val existingWebtoons = webtoons.map { it.siteWebtoonId }
            .chunked(1000)
            .flatMap { batch -> webtoonRepository.findByPlatformAndSiteWebtoonIdIn(platform, batch) }

        val existingWebtoonMap = existingWebtoons.associateBy { it.siteWebtoonId to it.platform }

        val newOrUpdatedWebtoons = webtoons
            .filter { webtoon ->
                val existing = existingWebtoonMap[webtoon.siteWebtoonId to webtoon.platform]
                if (existing == null) { // 신규 웹툰
                    true
                } else if (existing != webtoon) { // 변경사항이 있는 기존 웹툰
                    true
                } else {
                    false
                }
            }
            .map { webtoon ->
                val existing = existingWebtoonMap[webtoon.siteWebtoonId to webtoon.platform]
                webtoon.copy(webtoonId = existing?.webtoonId) // ✅ 기존 ID 유지하여 업데이트
            }

        if (newOrUpdatedWebtoons.isNotEmpty()) {
            newOrUpdatedWebtoons.chunked(500).forEach { batch ->
                webtoonRepository.saveAll(batch)
            }
        }
    }
}
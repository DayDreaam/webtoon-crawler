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

        val existingWebtoonMap = existingWebtoons.associateBy { it.siteWebtoonId }

        val newOrUpdatedWebtoons = webtoons
            .filter { webtoon ->
                val existing = existingWebtoonMap[webtoon.siteWebtoonId]
                existing == null || existing != webtoon // ✅ 기존과 다를 때만 저장 대상
            }
            .map { webtoon ->
                val existing = existingWebtoonMap[webtoon.siteWebtoonId]
                webtoon.copy(webtoonId = existing?.webtoonId) // ✅ 기존 ID 유지하여 업데이트 가능
            }

        if (newOrUpdatedWebtoons.isNotEmpty()) {
            newOrUpdatedWebtoons.chunked(500).forEach { batch ->
                webtoonRepository.saveAll(batch)
            }
        }
    }
}
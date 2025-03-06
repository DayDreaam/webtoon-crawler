package com.example.demo.webtoon.service

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.enums.Platform
import com.example.demo.webtoon.repository.WebtoonRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class CommonService(
    private val webtoonRepository: WebtoonRepository
) {
    suspend fun saveWebtoons(webtoons: List<Webtoon>) {
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
                try {
                    retryBatchSave(batch)  // ✅ 개별 트랜잭션으로 저장
                } catch (e: Exception) {
                    println("❌ 저장 실패 (배치 롤백, 나머지는 계속 진행): ${e.message}")
                }
            }
        }
    }

    private suspend fun retryBatchSave(batch: List<Webtoon>, maxRetries: Int = 3) {
        var attempt = 0
        var delay = 1000L

        while (attempt < maxRetries) {
            try {
                saveBatch(batch)
                return
            } catch (e: Exception) {
                attempt++
                println("❌ 저장 실패 (시도 횟수: $attempt), 에러: ${e.message}. ${delay}ms 후 재시도")
                kotlinx.coroutines.delay(delay)
                delay *= 2
            }
        }
        println("🚨 최대 재시도 횟수 초과! 이 배치는 저장되지 않음.")
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveBatch(batch: List<Webtoon>) {
        webtoonRepository.saveAll(batch)
    }
}
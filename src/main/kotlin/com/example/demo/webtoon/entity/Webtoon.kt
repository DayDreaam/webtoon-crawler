package com.example.demo.webtoon.entity
import com.example.demo.webtoon.enums.Platform
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "webtoon", indexes = [
    Index(name = "idx_platform_site_webtoon_id", columnList = "platform, site_webtoon_id")
])
class Webtoon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var webtoonId: Long? = null,

    val webtoonName: String,
    val platform: Platform,
    val siteWebtoonId: Long,
    val webtoonLink: String,
    val thumbnailUrl: String,
    val author: String,
    val finished: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Webtoon) return false
        // DB에 저장된 경우: webtoonId가 같으면 동일한 웹툰
        if (webtoonId != null && other.webtoonId != null) {
            return webtoonId == other.webtoonId
        }
        // DB에 저장되지 않은 경우: 같은 siteId + 플랫폼이면 같은 웹툰으로 간주
        return siteWebtoonId == other.siteWebtoonId && platform == other.platform
    }

    override fun hashCode(): Int {
        return webtoonId?.hashCode() ?: Objects.hash(siteWebtoonId, platform)
    }
}
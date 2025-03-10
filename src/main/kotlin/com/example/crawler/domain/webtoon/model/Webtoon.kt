package com.example.crawler.domain.webtoon.model

import com.example.crawler.domain.webtoon.model.enums.Platform
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "webtoon", indexes = [
        Index(name = "idx_platform_site_webtoon_id", columnList = "platform, site_webtoon_id")
    ]
)
class Webtoon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "webtoon_id")
    var webtoonId: Long? = null,

    val webtoonName: String,
    @Enumerated(EnumType.STRING)
    val platform: Platform,
    val siteWebtoonId: Long,
    val webtoonLink: String,
    val thumbnailUrl: String,
    val authors: String,
    val finished: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Webtoon) return false
        return siteWebtoonId == other.siteWebtoonId &&
                platform == other.platform &&
                webtoonName == other.webtoonName &&
                thumbnailUrl == other.thumbnailUrl &&
                finished == other.finished
    }


    override fun hashCode(): Int {
        return webtoonId?.hashCode() ?: Objects.hash(siteWebtoonId, platform)
    }

    fun copy(
        webtoonId: Long? = this.webtoonId,
        webtoonName: String = this.webtoonName,
        platform: Platform = this.platform,
        siteWebtoonId: Long = this.siteWebtoonId,
        webtoonLink: String = this.webtoonLink,
        thumbnailUrl: String = this.thumbnailUrl,
        authors: String = this.authors,
        finished: Boolean = this.finished
    ): Webtoon {
        return Webtoon(
            webtoonId = webtoonId,
            webtoonName = webtoonName,
            platform = platform,
            siteWebtoonId = siteWebtoonId,
            webtoonLink = webtoonLink,
            thumbnailUrl = thumbnailUrl,
            authors = authors,
            finished = finished
        )
    }
}
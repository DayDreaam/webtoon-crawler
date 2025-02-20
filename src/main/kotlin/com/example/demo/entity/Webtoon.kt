package com.example.demo.entity
import jakarta.persistence.*

@Entity
@Table(name = "webtoon")
class Webtoon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var webtoonId: Long? = null,

    val webtoonName: String,
    val platform: String,
    val webtoonLink: String,
    val thumbnailUrl: String,
    val author: String,
    val finished: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Webtoon

        if (finished != other.finished) return false
        if (webtoonName != other.webtoonName) return false
        if (platform != other.platform) return false
        if (webtoonLink != other.webtoonLink) return false
        if (thumbnailUrl != other.thumbnailUrl) return false
        if (author != other.author) return false

        return true
    }

    override fun hashCode(): Int {
        var result = finished.hashCode()
        result = 31 * result + webtoonName.hashCode()
        result = 31 * result + platform.hashCode()
        result = 31 * result + webtoonLink.hashCode()
        result = 31 * result + thumbnailUrl.hashCode()
        result = 31 * result + author.hashCode()
        return result
    }
}
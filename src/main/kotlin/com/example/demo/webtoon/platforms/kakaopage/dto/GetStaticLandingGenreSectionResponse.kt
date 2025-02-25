package com.example.demo.webtoon.platforms.kakaopage.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetStaticLandingGenreSectionResponse(
    @JsonProperty("data") val data: Data
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data(
        @JsonProperty("staticLandingGenreSection") val staticLandingGenreSection: StaticLandingGenreSection
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class StaticLandingGenreSection(
        @JsonProperty("isEnd") val isEnd: Boolean,
        @JsonProperty("groups") val groups: List<StaticLandingGenreSectionGroup>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class StaticLandingGenreSectionGroup(
        @JsonProperty("items") val items: List<KakoPageStaticLandingGenreSectionItem>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakoPageStaticLandingGenreSectionItem(
        @JsonProperty("seriesId") val seriesId: Long
    )
}
package com.example.crawler.dto.kakaopage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetContentHomeOverviewResponse(
    @JsonProperty("data") val data: ContentHomeOverviewData
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ContentHomeOverviewData(
    @JsonProperty("contentHomeOverview") val contentHomeOverview: ContentHomeOverview
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ContentHomeOverview(
    @JsonProperty("content") val content: WebtoonContent
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WebtoonContent(
    @JsonProperty("title") val title: String,
    @JsonProperty("authors") val authors: String,
    @JsonProperty("onIssue") val onIssue: String,
    @JsonProperty("thumbnail") val thumbnail: String
)

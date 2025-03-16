package com.example.crawler.global.infrastructure.dto.kakaopage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class GetStaticLandingTodayNewLayout(
    @JsonProperty("data") val data: DataWrapper
)

data class DataWrapper(
    @JsonProperty("staticLandingTodayNewLayout") val staticLandingTodayNewLayout: StaticLandingTodayNewLayout
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class StaticLandingTodayNewLayout(
    @JsonProperty("sections") val sections: List<Section>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Section(
    @JsonProperty("groups") val groups: List<Group>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Group(
    @JsonProperty("items") val items: List<SeriesItem>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SeriesItem(
    @JsonProperty("eventLog") val eventLog: EventLog
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventLog(
    @JsonProperty("eventMeta") val eventMeta: EventMeta
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class EventMeta(
    @JsonProperty("series_id") val seriesId: String
)
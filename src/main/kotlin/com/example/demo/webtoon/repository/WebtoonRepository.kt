package com.example.demo.webtoon.repository

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.enums.Platform
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface WebtoonRepository : JpaRepository<Webtoon, Long> {
    @Query("SELECT w FROM Webtoon w WHERE w.platform = :platform AND w.siteWebtoonId IN :siteWebtoonIds")
    fun findByPlatformAndSiteWebtoonIdIn(
        @Param("platform") platform: Platform,
        @Param("siteWebtoonIds") siteWebtoonIds: List<Long>
    ): List<Webtoon>
}
package com.example.demo.webtoon.repository

import com.example.demo.webtoon.entity.Webtoon
import org.springframework.data.jpa.repository.JpaRepository

interface WebtoonRepository : JpaRepository<Webtoon, Long>{
    fun findByWebtoonNameIn(names: List<String>): List<Webtoon>
}
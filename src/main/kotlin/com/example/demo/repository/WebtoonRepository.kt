package com.example.demo.repository

import com.example.demo.entity.Webtoon
import org.springframework.data.jpa.repository.JpaRepository

interface WebtoonRepository : JpaRepository<Webtoon, Long>{
    fun findByWebtoonNameIn(names: List<String>): List<Webtoon>
}
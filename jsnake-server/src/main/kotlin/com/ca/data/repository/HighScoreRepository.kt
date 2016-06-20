package com.ca.data.repository

import com.ca.data.entity.Difficulty
import com.ca.data.entity.HighscoreEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface HighscoreRepository : JpaRepository<HighscoreEntity, Int> {
    fun findByDifficultyOrderByScoreDesc(difficulty: Difficulty, pageable: Pageable) : List<HighscoreEntity>
}
package com.ca.data.repository

import com.ca.data.entity.Difficulty
import com.ca.data.entity.HighscoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HighScoreRepository : JpaRepository<HighscoreEntity, Int> {
    fun findTop10ByDifficultyOrderByScoreDesc(difficulty: Difficulty) : List<HighscoreEntity>
}
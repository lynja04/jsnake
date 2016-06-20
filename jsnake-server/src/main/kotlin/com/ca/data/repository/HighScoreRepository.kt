package com.ca.data.repository

import com.ca.data.entity.HighscoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HighScoreRepository: JpaRepository<HighscoreEntity, Int> {

}
package com.ca.api.model

import com.ca.data.entity.Difficulty
import com.ca.data.entity.HighscoreEntity

class HighscoreModel(var username: String, var score: Int, var difficulty: String) {
    constructor(highscoreEntity: HighscoreEntity) : this(highscoreEntity.username, highscoreEntity.score, highscoreEntity.difficulty.toString())
}
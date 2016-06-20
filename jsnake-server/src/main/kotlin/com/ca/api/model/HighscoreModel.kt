package com.ca.api.model

import com.ca.data.entity.HighscoreEntity
import java.util.*

class HighscoreModel(var username: String, var score: Int, var difficulty: String, var timestamp: Date?) {
    constructor(highscoreEntity: HighscoreEntity) :
    this(highscoreEntity.username, highscoreEntity.score, highscoreEntity.difficulty.toString(), highscoreEntity.timestamp)
}
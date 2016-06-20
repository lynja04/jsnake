package com.ca.api

import com.ca.api.model.APIResponse
import com.ca.api.model.HighscoreModel
import com.ca.api.model.Message
import com.ca.api.model.MessageType
import com.ca.data.entity.Difficulty
import com.ca.data.entity.HighscoreEntity
import com.ca.data.repository.HighscoreRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
class HighscoreController @Autowired constructor(val highScoreRepository: HighscoreRepository) {

    val logger = LoggerFactory.getLogger(HighscoreController::class.java)

    @RequestMapping(value = "/highscores", method = arrayOf(RequestMethod.GET))
    fun getHighscoresByDifficulty(@RequestParam(value="difficulty", required = false, defaultValue = "medium") difficulty: String,
                                  @RequestParam(value="page", required = false, defaultValue = "0") page: Int,
                                  @RequestParam(value="size", required = false, defaultValue = "50") size: Int) : APIResponse {
        var difficultyEnum = Difficulty.medium
        try {
            difficultyEnum = Difficulty.valueOf(difficulty)
        } catch(ex : IllegalArgumentException) { }
        val highscores = highScoreRepository.findByDifficultyOrderByScoreDesc(difficultyEnum, PageRequest(page, size))
                .map { it -> HighscoreModel(it.username, it.score, it.difficulty.toString())}
        return APIResponse(true, mutableListOf(), highscores.toMutableList())
    }

    @RequestMapping(value = "/highscores", method = arrayOf(RequestMethod.POST))
    fun addHighscore(@RequestBody highscoreModel: HighscoreModel) : APIResponse {
        if(highscoreModel.username.length > 45) {
            return APIResponse(false, mutableListOf(Message(MessageType.ERROR, "Username is greater than 45 characters.")), mutableListOf())
        }
        val highscoreEntity = HighscoreEntity()
        highscoreEntity.username = highscoreModel.username
        highscoreEntity.score = highscoreModel.score
        try {
            highscoreEntity.difficulty = Difficulty.valueOf(highscoreModel.difficulty.toLowerCase())
        } catch(ex : IllegalArgumentException) {
            return APIResponse(false, mutableListOf(Message(MessageType.ERROR, "Invalid difficulty submitted.")), mutableListOf())
        }

        try {
            val highscore = HighscoreModel(highScoreRepository.save(highscoreEntity))
            return APIResponse(true, mutableListOf(Message(MessageType.INFO, "Successfully added highscore.")), mutableListOf(highscore))
        } catch(ex: DataAccessException) {
            logger.error("Error while saving highscore.", ex)
            return APIResponse(false, mutableListOf(Message(MessageType.ERROR, "Error while saving highscore.")), mutableListOf())
        }
    }
}
package com.ca.api

import com.ca.api.model.APIResponse
import com.ca.api.model.HighscoreModel
import com.ca.data.entity.Difficulty
import com.ca.data.repository.HighScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HighscoreController @Autowired constructor(val highScoreRepository: HighScoreRepository) {

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
}
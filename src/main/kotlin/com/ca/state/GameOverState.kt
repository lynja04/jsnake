package com.ca.state

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox

class GameOverState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    var score: Int = 0
    var difficulty: String = ""
    @FXML
    lateinit var scoreLabel: Label

    init {
        score = params?.get("score")?.toInt() ?: 0
        difficulty = params?.get("difficulty") ?: "medium"
    }

    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(GameOverState::class.java.getResource("/views/gameOver.fxml"))
        fxmlLoader.setController(this)
        val highScorePane = fxmlLoader.load<VBox>()
        scoreLabel.text = "Your score: $score"
        scene = Scene(highScorePane)
    }

    override fun update() {
    }

    @FXML
    fun playAgain() {
        val params = mapOf("difficulty" to difficulty)
        val snakeState = SnakeState(gameStateManager, params)
        gameStateManager.changeState(snakeState)
    }

    @FXML
    fun mainMenu() {
        gameStateManager.changeState(MenuState(gameStateManager, null))
    }
}
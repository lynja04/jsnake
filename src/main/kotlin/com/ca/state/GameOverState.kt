package com.ca.state

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane

class GameOverState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    var score: Int = 0
    @FXML
    lateinit var scoreLabel: Label
    lateinit var highScoreLabel: Label
    lateinit var playAgainButton: Button

    override fun init(args: Map<String, String>?) {
        score = args?.get("score")?.toInt() ?: 0
        val fxmlLoader = FXMLLoader(GameOverState::class.java.getResource("/views/gameOver.fxml"))
        fxmlLoader.setController(this)
        val highScorePane = fxmlLoader.load<BorderPane>()
        scoreLabel.text = "Your score: $score"
        highScoreLabel.text = "Top 10 Scores:"
        playAgainButton.text = "Play Again?"
        playAgainButton.onMouseClicked = EventHandler { update() }
        scene = Scene(highScorePane)
    }

    override fun update() {
        val params = mapOf("hello" to "bye")
        val menuState = MenuState(gameStateManager, params)
        gameStateManager.changeState(menuState)
    }
}
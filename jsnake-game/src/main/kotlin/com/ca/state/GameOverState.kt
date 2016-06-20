package com.ca.state

import com.beust.klaxon.*
import com.ca.model.HighscoreModel
import com.github.kittinunf.fuel.Fuel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.layout.VBox
import java.util.*

class GameOverState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    var score: Int = 0
    var difficulty: String = ""
    @FXML
    lateinit var scoreLabel: Label
    @FXML
    lateinit var scoreBoardLabel: Label
    @FXML
    lateinit var errorLabel: Label
    @FXML
    lateinit var highScoreTable: TableView<HighscoreModel>

    init {
        score = params?.get("score")?.toInt() ?: 0
        difficulty = params?.get("difficulty") ?: "medium"
        scoreLabel.text = "Your score: $score"
        scoreBoardLabel.text = "Top 10 scores for ${difficulty.capitalize()}:"
        Fuel.get("/highscores", listOf(Pair("difficulty", difficulty), Pair("size", 10))).responseString {
            request, response, result ->
            result.fold({ r ->
                val resultJson = Parser().parse(r.byteInputStream()) as JsonObject
                val dataArray = resultJson.array<JsonObject>("data")
                if(dataArray != null) {
                    val highscoreModels = dataArray.map {
                        it -> HighscoreModel(it.string("username")!!, it.int("score")!!, it.string("timestamp")!!)
                    }
                    highScoreTable.items = FXCollections.observableList(highscoreModels)
                }
            }, { err ->
                Platform.runLater {
                    errorLabel.text = "Error retrieving high scores."
                }
            })
        }
    }

    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(GameOverState::class.java.getResource("/views/gameOver.fxml"))
        fxmlLoader.setController(this)
        val highScorePane = fxmlLoader.load<VBox>()
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
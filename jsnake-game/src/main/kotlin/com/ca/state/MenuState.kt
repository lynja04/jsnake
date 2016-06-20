package com.ca.state

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.RadioButton
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox

class MenuState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    @FXML
    lateinit var difficultyBtnsGroup: ToggleGroup

    override fun update() {
    }

    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(MenuState::class.java.getResource("/views/mainMenu.fxml"))
        fxmlLoader.setController(this)
        val menuPane = fxmlLoader.load<VBox>()
        scene = Scene(menuPane)
    }

    @FXML
    fun startGame() {
        val selectedDifficulty = difficultyBtnsGroup.selectedToggle as RadioButton
        val params = mapOf("difficulty" to selectedDifficulty.text.toLowerCase())
        val snakeState = SnakeState(gameStateManager, params)
        gameStateManager.changeState(snakeState)
    }
}
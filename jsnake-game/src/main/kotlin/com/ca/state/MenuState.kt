package com.ca.state

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox

class MenuState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    @FXML
    lateinit var difficultyBtnsGroup: ToggleGroup
    @FXML
    lateinit var usernameTextField: TextField

    init {
        usernameTextField.textProperty().addListener(ChangeListener<String> { observableValue: ObservableValue<out String>, oldValue: String, newValue: String ->
            if(usernameTextField.text.length > 45) {
                val trimmedText = usernameTextField.text.substring(0, 45)
                usernameTextField.text = trimmedText
            }
        })
        usernameTextField.text = params?.get("username") ?: "username"
    }

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
        val params = mapOf("difficulty" to selectedDifficulty.text.toLowerCase(), "username" to usernameTextField.text)
        val snakeState = SnakeState(gameStateManager, params)
        gameStateManager.changeState(snakeState)
    }
}
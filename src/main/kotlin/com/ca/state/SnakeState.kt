package com.ca.state

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox

class SnakeState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {
    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(MenuState::class.java.getResource("/views/snakeGame.fxml"))
        fxmlLoader.setController(this)
        val menuPane = fxmlLoader.load<VBox>()
        scene = Scene(menuPane)
    }

    override fun update() {
    }
}
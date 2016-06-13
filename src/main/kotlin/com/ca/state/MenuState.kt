package com.ca.state

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox

class MenuState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {
    override fun update() {
    }

    override fun init(args: Map<String, String>?) {
        val res = MenuState::class.java.getResource("/views/mainMenu.fxml")
        val menuPane = FXMLLoader.load<VBox>(res)
        scene = Scene(menuPane)
    }
}
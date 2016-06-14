package com.ca.state

import com.ca.snake.Grid
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane

class SnakeState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    val cellGrid: Grid = Grid(20, 20)

    companion object {
        val BLOCK_SIZE = 40.0
    }

    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(MenuState::class.java.getResource("/views/snakeGame.fxml"))
        fxmlLoader.setController(this)
        val snakePane = fxmlLoader.load<BorderPane>()
        scene = Scene(snakePane)
    }

    override fun update() {
    }
}
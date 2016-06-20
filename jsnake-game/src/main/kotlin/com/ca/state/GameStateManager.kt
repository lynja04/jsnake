package com.ca.state

import javafx.stage.Stage

class GameStateManager(val stage: Stage) {
    var gameRunning = true
    var curState: GameState? = null

    fun changeState(nextGameState: GameState) {
        stage.scene = nextGameState.scene
        curState = nextGameState
    }

    fun update() {
        curState?.update()
    }
}
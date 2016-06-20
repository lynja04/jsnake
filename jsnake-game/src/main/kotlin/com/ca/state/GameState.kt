package com.ca.state

import javafx.scene.Scene

abstract class GameState(val gameStateManager: GameStateManager, params: Map<String, String>?) {
    var scene: Scene? = null

    init {
        init(params)
    }

    abstract fun init(args: Map<String, String>?)
    abstract fun update()
}
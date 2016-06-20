package com.ca

import com.ca.state.GameStateManager
import com.ca.state.MenuState
import com.github.kittinunf.fuel.core.FuelManager
import javafx.application.Application
import javafx.stage.Stage

class SnakeApplication: Application() {

    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            FuelManager.instance.basePath = "http://192.168.1.204:8080/api"
            launch(SnakeApplication::class.java)
        }
    }

    override fun start(primaryStage: Stage?) {
        primaryStage?.isResizable = false
        primaryStage?.title = "JSnake"
        primaryStage?.centerOnScreen()
        val gsm = GameStateManager(primaryStage!!)
        val menuState = MenuState(gsm, null)
        gsm.changeState(menuState)
        primaryStage.show()
    }
}
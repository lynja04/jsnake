package com.ca.state

import com.ca.snake.Entity
import com.ca.snake.Grid
import com.ca.snake.SnakeEntity
import javafx.animation.AnimationTimer
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.transform.Rotate

class SnakeState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    val gridWidth = 20
    val gridLength = 20
    val cellGrid: Grid = Grid(gridWidth, gridLength)
    @FXML
    lateinit var gameCanvas: Canvas
    @FXML
    lateinit var startBtn: Button
    lateinit var graphicsContext: GraphicsContext

    init {
        cellGrid.cells[0][0].entity = SnakeEntity("/img/mouse.png", 0, 0, cellGrid, SnakeEntity.Type.HEAD, null, null)
        drawEntity(cellGrid.cells[0][0].entity!!)
    }

    companion object {
        val BLOCK_SIZE = 40.0
    }

    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(MenuState::class.java.getResource("/views/snakeGame.fxml"))
        fxmlLoader.setController(this)
        val snakePane = fxmlLoader.load<BorderPane>()
        graphicsContext = gameCanvas.graphicsContext2D
        scene = Scene(snakePane)
    }

    @FXML
    fun start() {
        startBtn.isDisable = true
        object : AnimationTimer() {
            override fun handle(now: Long) {
                update()
            }
        }.start()
    }

    override fun update() {
        graphicsContext.clearRect(0.0, 0.0, 800.0, 800.0)
        val entityList = mutableListOf<Entity>()
        for(i in 0..gridWidth - 1) {
            for(j in 0..gridLength - 1) {
                val curEntity = cellGrid.cells[i][j].entity
                if(curEntity != null) {
                    if(!curEntity.updated) {
                        curEntity.update()
                        curEntity.updated = true
                        entityList.add(curEntity)
                        drawEntity(curEntity)
                    }
                }
            }
        }
        for(e in entityList) {
            e.updated = false
        }
        Thread.sleep(50)
    }

    fun drawEntity(entity: Entity) {
        graphicsContext.drawImage(entity.image, entity.x * BLOCK_SIZE, entity.y * BLOCK_SIZE)
    }
}
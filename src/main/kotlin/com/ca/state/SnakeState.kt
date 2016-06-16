package com.ca.state

import com.ca.snake.Entity
import com.ca.snake.Grid
import com.ca.snake.MouseEntity
import com.ca.snake.SnakeEntity
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.util.Duration
import java.util.*

class SnakeState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    val gridWidth = 20
    val gridHeight = 20
    val cellGrid: Grid = Grid(gridWidth, gridHeight)
    val snakeHead: SnakeEntity
    val mouseEntity: MouseEntity
    var gameOver = false
    @FXML
    lateinit var gameCanvas: Canvas
    @FXML
    lateinit var startBtn: Button
    @FXML
    lateinit var scoreNumber: Label
    lateinit var graphicsContext: GraphicsContext

    init {
        snakeHead = SnakeEntity("/img/snakehead_right.png", 0, 0, cellGrid, SnakeEntity.Type.HEAD, null, null)
        cellGrid.cells[0][0].entity = snakeHead
        val random: Random = Random()
        val randX = random.nextInt(gridWidth)
        val randY = random.nextInt(gridHeight)
        mouseEntity = MouseEntity("/img/mouse.png", randX, randY, cellGrid)
        cellGrid.cells[randX][randY].entity = mouseEntity
        drawEntity(cellGrid.cells[0][0].entity!!)
        drawEntity(cellGrid.cells[randX][randY].entity!!)
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
        val timeLine = Timeline()
        timeLine.cycleCount = Timeline.INDEFINITE
        timeLine.keyFrames += KeyFrame(Duration.seconds(0.2), object: EventHandler<ActionEvent> {
            override fun handle(event: ActionEvent?) {
                if(!gameOver) {
                    update()
                } else {
                    timeLine.stop()
                }
            }
        })
        scene?.onKeyPressed = EventHandler { snakeHead.handleInput(it.code) }
        timeLine.play()
    }

    override fun update() {
        graphicsContext.clearRect(0.0, 0.0, gridWidth * BLOCK_SIZE, gridHeight * BLOCK_SIZE)
        var curSnakeEntity: SnakeEntity? = snakeHead
        while(curSnakeEntity != null) {
            if(snakeHead.state != SnakeEntity.State.DEAD) {
                curSnakeEntity.update()
            }
            drawEntity(curSnakeEntity)
            curSnakeEntity = curSnakeEntity.next
        }
        drawEntity(mouseEntity)
        scoreNumber.text = "" + snakeHead.score
        if(snakeHead.state == SnakeEntity.State.DEAD) {
            gameOver = true
        }
    }

    fun drawEntity(entity: Entity) {
        graphicsContext.drawImage(entity.image, entity.x * BLOCK_SIZE, entity.y * BLOCK_SIZE)
    }
}
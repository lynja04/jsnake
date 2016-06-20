package com.ca.state

import com.beust.klaxon.JsonObject
import com.ca.snake.Entity
import com.ca.snake.Grid
import com.ca.snake.MouseEntity
import com.ca.snake.SnakeEntity
import com.github.kittinunf.fuel.Fuel
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Platform
import javafx.concurrent.Task
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.util.Duration
import java.util.*

class SnakeState(gameStateManager: GameStateManager, params: Map<String, String>?) : GameState(gameStateManager, params) {

    val gridWidth = 20
    val gridHeight = 20
    val cellGrid: Grid = Grid(gridWidth, gridHeight)
    val snakeHead: SnakeEntity
    val mouseEntity: MouseEntity
    var gameOver = false
    val tickRate: Double
    val difficulty: String
    val username: String
    @FXML
    lateinit var gameCanvas: Canvas
    @FXML
    lateinit var startBtn: Button
    @FXML
    lateinit var scoreNumber: Label
    @FXML
    lateinit var gamePane: StackPane
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
        difficulty = params?.get("difficulty")?.toLowerCase() ?: "medium"
        username = params?.get("username") ?: "username"
        when(difficulty) {
            "easy" -> tickRate = 0.15
            "medium" -> tickRate = 0.09
            "hard" -> tickRate = 0.06
            else -> tickRate = 0.09
        }
    }

    companion object {
        val BLOCK_SIZE = 40.0
    }

    override fun init(args: Map<String, String>?) {
        val fxmlLoader = FXMLLoader(SnakeState::class.java.getResource("/views/snakeGame.fxml"))
        fxmlLoader.setController(this)
        val snakePane = fxmlLoader.load<BorderPane>()
        graphicsContext = gameCanvas.graphicsContext2D
        scene = Scene(snakePane)
    }

    @FXML
    fun start() {
        startBtn.isDisable = true
        val updateTimeline = Timeline()
        updateTimeline.cycleCount = Timeline.INDEFINITE
        updateTimeline.keyFrames += KeyFrame(Duration.seconds(tickRate), EventHandler<ActionEvent> {
            actionEvent -> run {
                if(!gameOver) {
                    update()
                } else {
                    updateTimeline.stop()
                    val submitHighscoreTask =  object: Task<Void>() {
                        override fun call(): Void? {
                            val requestBody = JsonObject(mapOf("username" to username,
                                    "score" to snakeHead.score,
                                    "difficulty" to difficulty))
                            val (request, response, result) = Fuel.post("/highscores")
                                    .header("Content-Type" to "application/json")
                                    .body(requestBody.toJsonString())
                                    .responseString()
                            Platform.runLater {
                                val params = mapOf("score" to snakeHead.score.toString(), "difficulty" to difficulty)
                                val gameOverState = GameOverState(gameStateManager, params)
                                gameStateManager.changeState(gameOverState)
                            }
                            return null
                        }
                    }
                    val progressIndicator = ProgressIndicator()
                    progressIndicator.progressProperty().bind(submitHighscoreTask.progressProperty())

                    val progressContainer = HBox(20.0)
                    progressContainer.id = "progressContainer"
                    progressContainer.alignment = Pos.CENTER
                    val uploadingText = Text("Uploading highscore...")
                    uploadingText.id = "progressText"
                    uploadingText.fill = Color.WHITE
                    progressContainer.children += uploadingText
                    progressContainer.children += progressIndicator
                    gamePane.children += progressContainer
                    Thread(submitHighscoreTask).start()
                }
            }
        })
        scene?.onKeyPressed = EventHandler { snakeHead.handleInput(it.code) }
        updateTimeline.play()
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
        scoreNumber.text = snakeHead.score.toString()
        if(snakeHead.state == SnakeEntity.State.DEAD) {
            gameOver = true
        }
    }

    fun drawEntity(entity: Entity) {
        graphicsContext.drawImage(entity.image, entity.x * BLOCK_SIZE, entity.y * BLOCK_SIZE)
    }
}
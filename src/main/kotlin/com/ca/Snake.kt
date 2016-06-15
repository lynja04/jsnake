package com.ca

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Color.*
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.util.Duration

/**
 * Created by Jimmy on 6/8/2016.
 */

class Snake() : Application(){

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    val BLOCK_SIZE = 40.0
    val WIDTH = 20 * BLOCK_SIZE
    val HEIGHT = 20 * BLOCK_SIZE

    var snake: ObservableList<Node> = FXCollections.observableArrayList()
    var moved = false
    var running = false
    var score = 0

    val game = Timeline()

    val initialSpeed = 0.09
    var TICK = Duration.seconds(initialSpeed)

    var direction = Direction.RIGHT

    fun createContent(diffLevel: Int) : Parent {
        val image = Image("img/logo.png")
        val logo = Rectangle(BLOCK_SIZE + 20, BLOCK_SIZE + 20)
        val imagePat = ImagePattern(image)
        logo.fill = imagePat
        logo.arcHeight = 40.0
        logo.arcWidth = 40.0
        val border = BorderPane()
        val hbox = HBox()
        hbox.padding = Insets(15.0, 12.0, 15.0, 12.0);
        hbox.spacing = 275.0;
        hbox.style = "-fx-background-color: #000000;"
        val startBtn = Button()
        startBtn.style = "-fx-font-size: 15px;-fx-font-weight: bold;"
        val scoreLbl = Label("Score: 0")
        scoreLbl.style = "-fx-font-size: 20px;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;"
        scoreLbl.prefWidth = 100.0
        startBtn.text = "Start"
        startBtn.prefWidth = 100.0
        hbox.children += scoreLbl
        hbox.children += startBtn
        hbox.children += logo
        border.top = hbox
        val root = Pane()
        root.setPrefSize(WIDTH, HEIGHT)
        root.style = "-fx-background-image: url('img/game_bg.jpg');"
        border.center = root

        when(diffLevel){
            1 -> TICK = Duration.seconds(0.15)
            2 -> TICK = Duration.seconds(0.09)
            3 -> TICK = Duration.seconds(0.04)
        }

        startBtn.onMouseClicked = EventHandler {
            if(!running){
                running = true
                startBtn.text = "Pause"
            } else {
                running = false
                startBtn.text = "Start"
            }
        }

        val snakeBody = Group()
        snake = snakeBody.children

        val mouseImage = Image("img/mouse.png")
        val food = Rectangle(BLOCK_SIZE, BLOCK_SIZE)
        val imagePattern = ImagePattern(mouseImage)
        food.fill = imagePattern
        food.arcHeight = 40.0
        food.arcWidth = 40.0
        food.translateX = (Math.random() * WIDTH/BLOCK_SIZE).toInt() * BLOCK_SIZE
        food.translateY = (Math.random() * HEIGHT/BLOCK_SIZE).toInt() * BLOCK_SIZE

        game.keyFrames.add(KeyFrame(TICK, {
            if(!running){
                return@KeyFrame
            }

            val toRemove = snake.size > 1 //if the length is 2

            val tail = if(toRemove){
                snake.removeAt(snake.size - 1)
            } else {
                snake.get(0)
            }

            val tailX = tail.translateX
            val tailY = tail.translateY

            when(direction){
                Direction.UP -> {
                    tail.translateX = snake.get(0).translateX
                    tail.translateY = snake.get(0).translateY - BLOCK_SIZE
                }

                Direction.DOWN -> {
                    tail.translateX = snake.get(0).translateX
                    tail.translateY = snake.get(0).translateY + BLOCK_SIZE
                }

                Direction.LEFT -> {
                    tail.translateX = snake.get(0).translateX - BLOCK_SIZE
                    tail.translateY = snake.get(0).translateY
                }

                Direction.RIGHT -> {
                    tail.translateX = snake.get(0).translateX + BLOCK_SIZE
                    tail.translateY = snake.get(0).translateY
                }
            }

            moved = true

            if(toRemove){
                snake.add(0, tail)
            }

            //Collision detection, for the walls of the screen
            if(tail.translateX < 0 || tail.translateX - BLOCK_SIZE >= WIDTH || tail.translateY < 0 || tail.translateY >= HEIGHT){
                startBtn.text = "Start"
                scoreLbl.text = "Score: 0"
                createScoreSplash(score, border, hbox)
                restart()
            }

            //Collision detection, if the snake hits it's body
            for(rect in snake){
                if(rect != tail && tail.translateX == rect.translateX && tail.translateY == rect.translateY){
                    startBtn.text = "Start"
                    scoreLbl.text = "Score: 0"
                    createScoreSplash(score, border, hbox)
                    restart()
                    break
                }
            }
            //Collision detection, if the snake eats the food block
            if(tail.translateX == food.translateX && tail.translateY == food.translateY){
                food.translateX = (Math.random() * WIDTH/BLOCK_SIZE).toInt() * BLOCK_SIZE
                food.translateY = (Math.random() * HEIGHT/BLOCK_SIZE).toInt() * BLOCK_SIZE

                //add to the end of the snake
                val snakeAddition = Rectangle(BLOCK_SIZE, BLOCK_SIZE)
                snakeAddition.translateX = tailX
                snakeAddition.translateY = tailY
                snakeAddition.fill = SPRINGGREEN
                snake.add(snakeAddition)
                scoreLbl.text = "Score: ${++score}"
            }
        }, arrayOf()))

        game.cycleCount = Animation.INDEFINITE
        game.play()
        root.children.addAll(food, snakeBody)
        return border
    }

    override fun start(primaryStage: Stage?) {
        val btn = Button("Begin")
        val welcomeLbl = Label("Welcome to JSnake!")
        welcomeLbl.padding = Insets(0.0, 12.0, 15.0, 26.0)
        welcomeLbl.style = "-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #000000;"
        var difficultyLbl = Label()
        difficultyLbl.style = "-fx-font-size: 12px; -fx-font-weight: bold"
        val pane = BorderPane()
        pane.padding = Insets(45.0, 32.0, 45.0, 32.0)
        pane.style = "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #00FF7F, #000000)"

        var difficulty = 1
        var difficultyString: String

        val easyBtn = Button("Easy")
        easyBtn.style = "-fx-font-size: 12px;-fx-font-weight: bold;"
        val mediumBtn = Button("Medium")
        mediumBtn.style = "-fx-font-size: 12px;-fx-font-weight: bold;"
        val hardBtn = Button("Hard")
        hardBtn.style = "-fx-font-size: 12px;-fx-font-weight: bold;"

        easyBtn.onMouseClicked = EventHandler {
            difficulty = 1
            difficultyString = "Easy"
            difficultyLbl.text = "Difficulty: $difficultyString"
        }

        mediumBtn.onMouseClicked = EventHandler {
            difficulty = 2
            difficultyString = "Medium"
            difficultyLbl.text = "Difficulty: $difficultyString"
        }

        hardBtn.onMouseClicked = EventHandler {
            difficulty = 3
            difficultyString = "Hard"
            difficultyLbl.text = "Difficulty: $difficultyString"
        }

        var difficultyBox = HBox(easyBtn, mediumBtn, hardBtn)
        difficultyBox.padding = Insets(25.0, 12.0, 15.0, 18.0)
        btn.style = "-fx-font-size: 12px;-fx-font-weight: bold;"
        var vbox = VBox(difficultyLbl, btn)
        vbox.padding = Insets(15.0, 12.0, 15.0, 62.0)

        pane.top = welcomeLbl
        pane.center = difficultyBox
        pane.bottom = vbox
        pane.prefHeight = 250.0
        pane.prefWidth = 250.0

        val scene1 = Scene(pane)
        btn.onMouseClicked = EventHandler {
            val scene = Scene(createContent(difficulty))
            startGame()
            primaryStage?.scene = scene
            primaryStage?.centerOnScreen()
            scene.onKeyPressed = EventHandler{
                if(moved){
                    when(it.code){
                        KeyCode.UP -> if(direction != Direction.DOWN){
                            direction = Direction.UP
                        }
                        KeyCode.DOWN -> if(direction != Direction.UP){
                            direction = Direction.DOWN
                        }
                        KeyCode.LEFT -> if(direction != Direction.RIGHT){
                            direction = Direction.LEFT
                        }
                        KeyCode.RIGHT -> if(direction != Direction.LEFT){
                            direction = Direction.RIGHT
                        }
                        KeyCode.W -> if(direction != Direction.DOWN){
                            direction = Direction.UP
                        }
                        KeyCode.S -> if(direction != Direction.UP){
                            direction = Direction.DOWN
                        }
                        KeyCode.A -> if(direction != Direction.RIGHT){
                            direction = Direction.LEFT
                        }
                        KeyCode.D -> if(direction != Direction.LEFT){
                            direction = Direction.RIGHT
                        }

                    }
                }
                moved = false
            }
        }
        primaryStage?.title = "JSnake"
        primaryStage?.scene = scene1
        primaryStage?.isResizable = false
        primaryStage?.centerOnScreen()
        primaryStage?.show()
    }

    fun createScoreSplash(score: Int, border: BorderPane, root: Pane){
        var gridPane = GridPane()
        gridPane.hgap = 10.0
        gridPane.vgap = 10.0
        gridPane.padding = Insets(15.0, 12.0, 15.0, 12.0)
        var vbox = VBox()
        gridPane.style = "-fx-background-color: #000000;"
        var scoreLbl = Label("Your score was $score!")
        scoreLbl.style = "-fx-font-size: 23px;-fx-font-weight: bold;-fx-text-fill: #FFFFFF;"
        var exitBtn = Button("Restart")
        exitBtn.style = "-fx-font-size: 12px;-fx-font-weight: bold;"
        vbox.children += scoreLbl
        vbox.children += exitBtn
        gridPane.add(vbox, 29, 1)
        border.top = gridPane
        exitBtn.onMouseClicked = EventHandler {
            border.top = root
        }
    }

    fun restart(){
        stopGame()
        startGame()
    }

    fun stopGame(){
        running = false
        game.stop()
        snake.clear()
    }

    fun startGame(){
        score = 0
        direction = Direction.RIGHT
        val head = Rectangle(BLOCK_SIZE, BLOCK_SIZE)
        head.fill = Color.SPRINGGREEN
        head.arcWidth = 40.0
        snake.add(head)
        game.play()
    }

    companion object{
        @JvmStatic
        fun main(args: Array<String>){
            launch(Snake::class.java)
        }
    }
}

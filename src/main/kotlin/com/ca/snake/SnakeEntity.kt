package com.ca.snake

import javafx.scene.image.Image
import javafx.scene.input.KeyCode

class SnakeEntity(imageUrl: String, x: Int, y: Int, grid: Grid, var type: Type, var next: SnakeEntity?, var prev: SnakeEntity?) : Entity(imageUrl, x, y, grid) {

    var oldX: Int = 0
    var oldY: Int = 0
    var state: State = State.ALIVE
    var direction: Direction = Direction.RIGHT
    val images = mapOf(Direction.RIGHT to Image("/img/snakehead_right.png"),
            Direction.DOWN to Image("/img/snakehead_down.png"),
            Direction.LEFT to Image("/img/snakehead_left.png"),
            Direction.UP to Image("/img/snakehead_up.png"))

    override fun update() {
        when(type) {
            Type.HEAD -> {
                updateHead()
            }
            Type.TAIL, Type.BODY -> {
                updateBody()
            }
        }
    }

    fun updateBody() {
        when(state) {
            State.ALIVE -> {
                val curCell = grid.getCell(x, y)
                oldX = x
                oldY = y
                x = prev?.oldX!!
                y = prev?.oldY!!
                val nextCell = grid.getCell(x, y)
                nextCell.entity = curCell.entity
                curCell.entity = null
            }
        }
    }

    fun updateHead() {
        when (state) {
            State.ALIVE -> {
                val curCell = grid.getCell(x, y)
                oldX = x
                oldY = y
                var tempX = x
                var tempY = y
                when (direction) {
                    Direction.UP -> {
                        tempY--
                    }
                    Direction.RIGHT -> {
                        tempX++
                    }
                    Direction.DOWN -> {
                        tempY++
                    }
                    Direction.LEFT -> {
                        tempX--
                    }
                }
                if (tempX >= 0 && tempX < grid.width && tempY >= 0 && tempY < grid.height) {
                    val nextCell = grid.getCell(tempX, tempY)
                    val nextEntity = nextCell.entity
                    if (nextEntity != null && nextEntity is MouseEntity) {
                        var curSnake = this
                        while (curSnake.next != null) {
                            curSnake = curSnake.next!!
                        }
                        curSnake.next = SnakeEntity("/img/snakehead_right.png", curSnake.oldX, curSnake.oldY, grid, Type.TAIL, null, curSnake)
                        if (curSnake.type == Type.TAIL) {
                            curSnake.type = Type.BODY
                        }
                        nextEntity.state = MouseEntity.State.DEAD
                        nextEntity.update()
                    }
                    nextCell.entity = curCell.entity
                    curCell.entity = null
                    x = tempX
                    y = tempY
                } else {
                    state = State.DEAD
                }
            }
        }
    }

    fun handleInput(keyCode: KeyCode) {
        when(keyCode) {
            KeyCode.UP, KeyCode.W -> {
                if(direction != Direction.DOWN) {
                    direction = Direction.UP
                    image = images[Direction.UP]!!
                }
            }
            KeyCode.RIGHT, KeyCode.D -> {
                if(direction != Direction.LEFT) {
                    direction = Direction.RIGHT
                    image = images[Direction.RIGHT]!!
                }
            }
            KeyCode.DOWN, KeyCode.S -> {
                if(direction != Direction.UP) {
                    direction = Direction.DOWN
                    image = images[Direction.DOWN]!!
                }
            }
            KeyCode.LEFT, KeyCode.A -> {
                if(direction != Direction.RIGHT) {
                    direction = Direction.LEFT
                    image = images[Direction.LEFT]!!
                }
            }
        }
    }

    enum class Type {
        HEAD, BODY, TAIL
    }

    enum class Direction {
        UP, RIGHT, DOWN, LEFT
    }

    enum class State {
        ALIVE, DEAD
    }
}
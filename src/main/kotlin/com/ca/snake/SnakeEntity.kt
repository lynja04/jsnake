package com.ca.snake

import javafx.scene.image.Image
import javafx.scene.input.KeyCode

class SnakeEntity(imageUrl: String, x: Int, y: Int, grid: Grid, var type: Type, var next: SnakeEntity?, var prev: SnakeEntity?) : Entity(imageUrl, x, y, grid) {

    var oldX: Int = 0
    var oldY: Int = 0
    var state: State = State.ALIVE
    var direction: Direction = Direction.RIGHT
    val headImages = mapOf(Direction.RIGHT to Image("/img/snakehead_right.png"),
            Direction.DOWN to Image("/img/snakehead_down.png"),
            Direction.LEFT to Image("/img/snakehead_left.png"),
            Direction.UP to Image("/img/snakehead_up.png"))
    val tailImages = mapOf(Direction.RIGHT to Image("/img/snaketail_right.png"),
            Direction.DOWN to Image("/img/snaketail_down.png"),
            Direction.LEFT to Image("/img/snaketail_left.png"),
            Direction.UP to Image("/img/snaketail_up.png"))
    val bodyImages = mapOf(Direction.RIGHT to Image("/img/snake_horizontal.png"),
            Direction.LEFT to Image("/img/snake_horizontal.png"),
            Direction.UP to Image("/img/snake_vertical.png"),
            Direction.DOWN to Image("/img/snake_vertical.png"),
            Direction.UPLEFT to Image("/img/snakeelbow_up_left.png"),
            Direction.UPRIGHT to Image("/img/snakeelbow_up_right.png"),
            Direction.DOWNLEFT to Image("/img/snakeelbow_down_left.png"),
            Direction.DOWNRIGHT to Image("/img/snakeelbow_down_right.png"))

    override fun update() {
        when(type) {
            Type.HEAD -> {
                updateHead()
            }
            Type.BODY -> {
                updateBody()
            }
            Type.TAIL -> {
                updateTail()
            }
        }
    }

    fun updateTail() {
        when(state) {
            State.ALIVE -> {
                val curCell = grid.getCell(x, y)
                oldX = x
                oldY = y
                x = prev?.oldX!!
                y = prev?.oldY!!
                val prevX = prev?.x
                val prevY = prev?.y
                val nextCell = grid.getCell(x, y)
                nextCell.entity = curCell.entity
                curCell.entity = null
                if(x < prevX!!) {
                    image = tailImages[Direction.RIGHT]!!
                } else if(x > prevX!!) {
                    image = tailImages[Direction.LEFT]!!
                } else if(y < prevY!!) {
                    image = tailImages[Direction.DOWN]!!
                } else if(y > prevY!!) {
                    image = tailImages[Direction.UP]!!
                }
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
                val prevX = prev?.x
                val prevY = prev?.y
                val nextX = oldX
                val nextY = oldY
                if(prevY == nextY) {
                    image = bodyImages[Direction.RIGHT]!!
                } else if(prevX == nextX) {
                    image = bodyImages[Direction.UP]!!
                } else if((prevX!! < x && nextY < y) || (nextX < x && prevY!! < y)) {
                    image = bodyImages[Direction.DOWNLEFT]!!
                } else if((prevX!! > x && nextY < y) || (nextX > x && prevY!! < y)) {
                    image = bodyImages[Direction.DOWNRIGHT]!!
                } else if((nextX < x && prevY!! > y) || (prevX!! < x && nextY > y)) {
                    image = bodyImages[Direction.UPLEFT]!!
                } else if((nextX > x && prevY!! > y) || (prevX!! >x && nextY > y)) {
                    image = bodyImages[Direction.UPRIGHT]!!
                }
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
                    nextCell.entity = curCell.entity
                    curCell.entity = null
                    if (nextEntity != null && nextEntity is MouseEntity) {
                        var curSnake = this
                        while (curSnake.next != null) {
                            curSnake = curSnake.next!!
                        }
                        curSnake.next = SnakeEntity("/img/snaketail_right.png", curSnake.oldX, curSnake.oldY, grid, Type.TAIL, null, curSnake)
                        val curSnakeCell = grid.getCell(curSnake.oldX, curSnake.oldY)
                        curSnakeCell.entity = curSnake.next
                        if (curSnake.type == Type.TAIL) {
                            curSnake.type = Type.BODY
                        }
                        nextEntity.state = MouseEntity.State.DEAD
                        nextEntity.update()
                    }
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
                    image = headImages[Direction.UP]!!
                }
            }
            KeyCode.RIGHT, KeyCode.D -> {
                if(direction != Direction.LEFT) {
                    direction = Direction.RIGHT
                    image = headImages[Direction.RIGHT]!!
                }
            }
            KeyCode.DOWN, KeyCode.S -> {
                if(direction != Direction.UP) {
                    direction = Direction.DOWN
                    image = headImages[Direction.DOWN]!!
                }
            }
            KeyCode.LEFT, KeyCode.A -> {
                if(direction != Direction.RIGHT) {
                    direction = Direction.LEFT
                    image = headImages[Direction.LEFT]!!
                }
            }
        }
    }

    enum class Type {
        HEAD, BODY, TAIL
    }

    enum class Direction {
        UP, RIGHT, DOWN, LEFT, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT
    }

    enum class State {
        ALIVE, DEAD
    }
}
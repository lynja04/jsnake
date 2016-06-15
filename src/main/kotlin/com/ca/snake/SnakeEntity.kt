package com.ca.snake

import javafx.scene.input.KeyCode
import java.awt.event.KeyEvent

class SnakeEntity(imageUrl: String, x: Int, y: Int, grid: Grid, var type: Type, var next: SnakeEntity?, var prev: SnakeEntity?) : Entity(imageUrl, x, y, grid) {

    var state: State = State.ALIVE
    var direction: Direction = Direction.RIGHT

    override fun update() {
        when(state) {
            State.ALIVE -> {
                val curCell = grid.getCell(x, y)
                var tempX = x
                var tempY = y
                when(direction) {
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
                if(tempX >= 0 && tempX < grid.width && tempY >= 0 && tempY < grid.height) {
                    val nextCell = grid.getCell(tempX, tempY)
                    nextCell.entity = curCell.entity
                    curCell.entity = null
                    x = tempX
                    y = tempY
                } else {
                    state = State.DEAD
                }
            }
        }

        /*when(type) {
            Type.HEAD -> {
            }
            Type.BODY -> {

            }
            Type.TAIL -> {

            }
        }*/
    }

    fun handleInput(keyCode: KeyCode) {
        when(keyCode) {
            KeyCode.UP, KeyCode.W -> {
                direction = if(direction != Direction.DOWN) Direction.UP else direction
            }
            KeyCode.RIGHT, KeyCode.D -> {
                direction = if(direction != Direction.LEFT) Direction.RIGHT else direction
            }
            KeyCode.DOWN, KeyCode.S -> {
                direction = if(direction != Direction.UP) Direction.DOWN else direction
            }
            KeyCode.LEFT, KeyCode.A -> {
                direction = if(direction != Direction.RIGHT) Direction.LEFT else direction
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
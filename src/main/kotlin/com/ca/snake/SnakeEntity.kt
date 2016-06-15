package com.ca.snake

class SnakeEntity(imageUrl: String, x: Int, y: Int, grid: Grid, var type: Type, var next: SnakeEntity?, var prev: SnakeEntity?) : Entity(imageUrl, x, y, grid) {

    var state: State = State.ALIVE

    override fun update() {
        when(state) {
            State.ALIVE -> {
                val curCell = grid.getCell(x, y)
                var tempX = x + 1;
                var tempY = y + 1;
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

    enum class Type {
        HEAD, BODY, TAIL
    }

    enum class State {
        ALIVE, DEAD
    }
}
package com.ca.snake

import java.util.*

class MouseEntity(imageUrl: String, x: Int, y: Int, grid: Grid): Entity(imageUrl, x, y, grid) {

    var state: State = State.ALIVE
    val random: Random = Random()

    override fun update() {
        when(state) {
            State.DEAD -> {
                var cellFound = false
                do {
                    val randX = random.nextInt(grid.width)
                    val randY = random.nextInt(grid.height)
                    val randCell = grid.getCell(randX, randY)
                    if(randCell.entity == null) {
                        randCell.entity = this
                        x = randX
                        y = randY
                        cellFound = true
                        state = State.ALIVE
                    }
                } while(!cellFound)
            }
        }
    }

    enum class State {
        ALIVE, DEAD
    }
}
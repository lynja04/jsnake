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
                    val randX = random.nextInt(grid.width + 1)
                    val randY = random.nextInt(grid.height + 1)
                    val randCell = grid.getCell(randX, randY)
                    if(randCell.entity == null) {
                        randCell.entity = this
                        x = randX
                        y = randY
                        cellFound = true
                    }
                } while(!cellFound)
            }
        }
    }

    enum class State {
        ALIVE, DEAD
    }
}
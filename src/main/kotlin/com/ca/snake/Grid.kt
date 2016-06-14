package com.ca.snake

class Grid(width: Int, height: Int) {

    val cells: Array<Array<Cell>>

    init {
        cells = Array(width, { x -> Array(height, { y -> Cell(null) }) })
    }

    fun getCell(x: Int, y: Int) = cells[x][y]
}
package com.ca.snake

class Grid(val width: Int, val height: Int) {

    val cells: Array<Array<Cell>>

    init {
        cells = Array(width, { x -> Array(height, { y -> Cell(null) }) })
    }

    fun getCell(x: Int, y: Int) = cells[x][y]
}
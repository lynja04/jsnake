package com.ca.snake

import javafx.scene.image.Image

abstract class Entity(imageUrl: String, var x: Int, var y: Int, val grid: Grid) {

    var image: Image
    var updated: Boolean = false

    init {
        image = Image(imageUrl)
    }

    abstract fun update()
}
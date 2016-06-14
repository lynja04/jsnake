package com.ca.snake

import javafx.scene.image.ImageView

class Entity(imageUrl: String) {

    val image: ImageView

    init {
        image = ImageView(imageUrl)
    }
}
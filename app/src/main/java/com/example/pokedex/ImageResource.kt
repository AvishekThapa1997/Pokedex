package com.example.pokedex

import kotlin.random.Random
import kotlin.random.nextInt

object ImageResource {
    private val previousIndex = -1;
    private val imageResource = listOf(
        0
    )

    val randomImage: Int
        get() {
            var newIndex = 0
            while (true) {
                newIndex = Random.nextInt(imageResource.indices)
                if (newIndex != previousIndex) break
            }
            return imageResource[newIndex]
        }
}
package com.github.harmittaa.touchobserver.model

data class SingleEvent(
    val xPos: Float = 0f,
    val yPos: Float = 0f,
    val size: Float = 0f,
    val xVelocity: Float = 0f,
    val yVelocity: Float = 0f,
    val time: Long = 0
)
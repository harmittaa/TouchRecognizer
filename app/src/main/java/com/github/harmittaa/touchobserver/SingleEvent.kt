package com.github.harmittaa.touchobserver

data class SingleEvent(
    val xPos: Float,
    val yPos: Float,
    val size: Float,
    val xVelocity: Float,
    val yVelocity: Float,
    val time: Long
)
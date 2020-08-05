package com.github.harmittaa.touchobserver.model

import com.google.firebase.database.Exclude

data class TouchGesture(
    @Exclude
    val touchPoints: List<SingleEvent>,
    val width: Float = 0f,
    val height: Float  = 0f,
    val euclideanDistance: Float  = 0f,
    val durationMs: Long = 0,
    val maxVelocityPx100ms: Float  = 0f,
    val avgVelocityPx100mx: Double = 0.0,
    val maxAcceleration: Float  = 0f,
    val swipeCurvature: Float  = 0f
)
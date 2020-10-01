package com.github.harmittaa.touchobserver.model

import com.github.harmittaa.touchobserver.logic.GestureDirection
import com.google.firebase.database.Exclude

data class TouchGesture(
    val gestureDirection: GestureDirection = GestureDirection.UNKNOWN,
    @Exclude
    val touchPoints: List<SingleEvent>,
    val width: Float = 0f,
    val height: Float = 0f,
    val euclideanDistance: Float = 0f,
    val durationMs: Long = 0,
    val maxVelocityPx100ms: Float = 0f,
    val avgVelocityPx100ms: Double = 0.0,
    val yMaxAcceleration: Float = 0f,
    val xMaxAcceleration: Float = 0f,
    val swipeCurvature: Float = 0f
)

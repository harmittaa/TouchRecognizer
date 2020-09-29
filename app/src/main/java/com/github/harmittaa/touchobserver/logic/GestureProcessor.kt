package com.github.harmittaa.touchobserver.logic

import com.github.harmittaa.touchobserver.model.SingleEvent
import com.github.harmittaa.touchobserver.model.TouchGesture
import com.github.harmittaa.touchobserver.screens.swipe.GestureDirection
import kotlin.math.atan
import kotlin.math.withSign
import timber.log.Timber

class GestureProcessor {

    private fun resolveGestureDirection(
        start: SingleEvent,
        end: SingleEvent
    ): GestureDirection {
        val verticalChange = (start.yPos - end.yPos).withSign(1)
        val horizontalChange = (start.xPos - end.xPos).withSign(1)

        return if (verticalChange > horizontalChange) {
            when {
                start.yPos < end.yPos -> {
                    GestureDirection.UP_DOWN
                }
                start.yPos > end.yPos -> {
                    GestureDirection.DOWN_UP
                }
                else -> {
                    Timber.d("Woops, something weird with that gesture!")
                    GestureDirection.UNKNOWN
                }
            }
        } else {
            when {
                start.xPos > end.xPos -> {
                    GestureDirection.RIGHT_LEFT
                }
                start.xPos < end.xPos -> {
                    GestureDirection.LEFT_RIGHT
                }
                else -> {
                    Timber.d("Woops, something weird with that gesture!")
                    GestureDirection.UNKNOWN
                }
            }
        }
    }

    fun validateGesture(gestureList: MutableList<SingleEvent>): TouchGesture? {
        // TODO
        if (gestureList.count() <= 2) return null

        val startPoint = gestureList.first()
        val endPoint = gestureList.last()

        val direction: GestureDirection = resolveGestureDirection(startPoint, endPoint)

        // total duration
        val durationMs = endPoint.time - startPoint.time
        // total euclidean distance
        val totalDistancePx = getEuclideanDistance(startPoint, endPoint, direction)
        val maxVelocityPx100ms = getMaxVelocityPx100ms(gestureList, direction)
        val averageVelocityPx100ms = getAverageVelocityPx100ms(gestureList, direction)
        // width of the swipe
        val width =
            (gestureList.maxByOrNull { it.xPos }?.xPos ?: 0f) - (gestureList.minByOrNull { it.xPos }?.xPos
                ?: 0f)
        // height of the swipe
        val height =
            (gestureList.maxByOrNull { it.yPos }?.yPos ?: 0f) - (gestureList.minByOrNull { it.yPos }?.yPos
                ?: 0f)

        var yAccelerationPixelsPer100ms = 0f
        var xAccelerationPixelsPer100ms = 0f
        for (x in 0 until gestureList.count()) {
            if (x == 0) {
                continue
            }
            val event = gestureList[x]
            val prevEvent = gestureList[x - 1]
            val timeDiff = event.time - prevEvent.time

            if (event.yVelocity == 0f || event.xVelocity == 0f || timeDiff == 0L) {
/*
                Timber.d(
                    "Velocity is 0 or time diff is 0. TimeDiff: $timeDiff," +
                            "event vel: ${event.yVelocity} prev vel: ${prevEvent.yVelocity} this is event $x"
                )
*/
                continue
            }

            val yCurrAcceleration =
                (event.yVelocity.withSign(1) - prevEvent.yVelocity.withSign(1)) / timeDiff
            val xCurrAcceleration =
                (event.xVelocity.withSign(1) - prevEvent.xVelocity.withSign(1)) / timeDiff

            if (yCurrAcceleration > yAccelerationPixelsPer100ms) {
                yAccelerationPixelsPer100ms = yCurrAcceleration
            }
            if (xCurrAcceleration > xAccelerationPixelsPer100ms) {
                xAccelerationPixelsPer100ms = xCurrAcceleration
            }
        }

        // calculate swipe curvature

        val curvature = calculateCurvature(startPoint, endPoint, direction)

        val touchGesture = TouchGesture(
            gestureDirection = direction,
            touchPoints = gestureList,
            width = width,
            height = height,
            euclideanDistance = totalDistancePx,
            durationMs = durationMs,
            maxVelocityPx100ms = maxVelocityPx100ms,
            avgVelocityPx100ms = averageVelocityPx100ms,
            yMaxAcceleration = yAccelerationPixelsPer100ms,
            xMaxAcceleration = xAccelerationPixelsPer100ms,
            swipeCurvature = curvature
        )
        return touchGesture
    }

    private fun calculateCurvature(
        start: SingleEvent,
        end: SingleEvent,
        direction: GestureDirection
    ): Float {
        // atan((startPoint.yPos - endPoint.yPos) / (startPoint.xPos - endPoint.xPos))
        return when (direction) {
            GestureDirection.DOWN_UP -> {
                atan((start.yPos - end.yPos) / (start.xPos - end.xPos))
            }
            GestureDirection.UP_DOWN -> {
                atan((end.yPos - start.yPos) / (end.xPos - start.xPos))
            }
            GestureDirection.LEFT_RIGHT -> {
                atan((start.xPos - end.xPos) / (start.yPos - end.yPos))
            }
            GestureDirection.RIGHT_LEFT -> {
                atan((end.xPos - start.xPos) / (end.yPos - start.yPos))
            }
            GestureDirection.UNKNOWN -> {
                0f
            }
        }
    }

    private fun getAverageVelocityPx100ms(
        gestureList: MutableList<SingleEvent>,
        direction: GestureDirection
    ): Double {
        return when (direction) {
            GestureDirection.DOWN_UP, GestureDirection.UP_DOWN -> {
                // sign to convert to positive, down up is always negative
                gestureList.map { it.yVelocity.withSign(1) }.average()
            }
            GestureDirection.LEFT_RIGHT, GestureDirection.RIGHT_LEFT -> {
                gestureList.map { it.xVelocity.withSign(1) }.average()
            }
            GestureDirection.UNKNOWN -> {
                0.0
            }
        }
    }

    // max velocity [speed]
    private fun getMaxVelocityPx100ms(
        gestureList: MutableList<SingleEvent>,
        direction: GestureDirection
    ): Float {
        return when (direction) {
            GestureDirection.DOWN_UP, GestureDirection.UP_DOWN -> {
                // sign to convert to positive, down up is always negative
                gestureList.maxByOrNull { event -> event.yVelocity.withSign(1) }?.yVelocity?.withSign(1)
                    ?: 0f
            }
            GestureDirection.LEFT_RIGHT, GestureDirection.RIGHT_LEFT -> {
                gestureList.maxByOrNull { event -> event.xVelocity.withSign(1) }?.xVelocity?.withSign(1)
                    ?: 0f
            }
            GestureDirection.UNKNOWN -> {
                0f
            }
        }
    }

    private fun getEuclideanDistance(
        start: SingleEvent,
        end: SingleEvent,
        direction: GestureDirection
    ): Float {
        return when (direction) {
            GestureDirection.DOWN_UP, GestureDirection.UP_DOWN -> {
                (start.yPos - end.yPos).withSign(1)
            }
            GestureDirection.LEFT_RIGHT, GestureDirection.RIGHT_LEFT -> {
                (start.xPos - end.xPos).withSign(1)
            }
            GestureDirection.UNKNOWN -> {
                0f
            }
        }
    }
}

package com.github.harmittaa.touchobserver

import androidx.lifecycle.*
import com.github.harmittaa.touchobserver.model.SingleEvent
import com.github.harmittaa.touchobserver.model.TouchGesture
import com.github.harmittaa.touchobserver.repository.TouchRepository
import timber.log.Timber
import kotlin.math.atan
import kotlin.math.withSign

const val SWIPE_THRESHOLD = 500
const val SWIPE_REQUIRED_COUNT = 5

enum class SwipeStyle {
    SWIPE_DOWN_UP,
    SWIPE_UP_DOWN,
    SWIPE_LEFT_RIGHT,
    SWIPE_RIGHT_LEFT
}


class SwipeViewModel(private val repository: TouchRepository) : ViewModel() {
    private val allGestures = mutableListOf<TouchGesture>()
    var gestureCount: MutableLiveData<Int> = MutableLiveData()
    var canContinue: MutableLiveData<Boolean> = MutableLiveData(false)


    fun storeGesture(gestureList: MutableList<SingleEvent>) {
        validateGesture(gestureList)
        Timber.d("STORING ${allGestures.count()} items")
        if (allGestures.count() >= SWIPE_REQUIRED_COUNT) {
            canContinue.value = true
            repository.storeSwipes(SwipeStyle.SWIPE_DOWN_UP, allGestures.toList())
            allGestures.clear()
            uploadData()
        }
    }

    private fun uploadData() {
        repository.storeSwipes(SwipeStyle.SWIPE_DOWN_UP, allGestures)
    }

    private fun validateGesture(gestureList: MutableList<SingleEvent>) {
        val startPoint = gestureList.first()
        val endPoint = gestureList.last()

        if (startPoint.yPos - endPoint.yPos >= SWIPE_THRESHOLD) {
            // total duration
            val durationMs = endPoint.time - startPoint.time
            // total euclidean distance
            val totalDistancePx = startPoint.yPos - endPoint.yPos
            // max velocity [speed] (sign to convert to positive, down up is always negative)
            val maxVelocityPx100ms =
                gestureList.maxBy { event -> event.yVelocity.withSign(1) }?.yVelocity?.withSign(1)
                    ?: 0f
            val averageVelocityPx100mx = gestureList.map { it.yVelocity.withSign(1) }.average()
            // width of the swipe
            val width =
                (gestureList.maxBy { it.xPos }?.xPos ?: 0f) - (gestureList.minBy { it.xPos }?.xPos
                    ?: 0f)
            // height of the swipe
            val height = startPoint.yPos - endPoint.yPos

            var accelerationPixelsPer100ms = 0f
            for (x in 0 until gestureList.count()) {
                if (x == 0) {
                    continue
                }
                val event = gestureList[x]
                val prevEvent = gestureList[x - 1]
                val timeDiff = event.time - prevEvent.time

                if (event.yVelocity == 0f || timeDiff == 0L) {
                    Timber.d("Velocity is 0 or time diff is 0. TimeDiff: $timeDiff," +
                            "event vel: ${event.yVelocity} prev vel: ${prevEvent.yVelocity} this is event $x ")
                    continue
                }

                val currAcceleration =
                    (event.yVelocity.withSign(1) - prevEvent.yVelocity.withSign(1)) / timeDiff
                if (currAcceleration > accelerationPixelsPer100ms) {
                    accelerationPixelsPer100ms = currAcceleration
                }
            }

            // calculate swipe curvature
            val curvature =
                atan((startPoint.yPos - endPoint.yPos) / (startPoint.xPos - endPoint.xPos))


            val touchGesture = TouchGesture(
                touchPoints = gestureList,
                width = width,
                height = height,
                euclideanDistance = totalDistancePx,
                durationMs = durationMs,
                maxVelocityPx100ms = maxVelocityPx100ms,
                avgVelocityPx100mx = averageVelocityPx100mx,
                maxAcceleration = accelerationPixelsPer100ms,
                swipeCurvature = curvature
            )
            allGestures.add(touchGesture)
            gestureCount.value = allGestures.count()
        }
    }
}
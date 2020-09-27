package com.github.harmittaa.touchobserver.screens.webview

import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.lifecycle.ViewModel
import com.github.harmittaa.touchobserver.model.SingleEvent
import timber.log.Timber

class TouchRegisterViewModel : ViewModel() {
    private var gestureList = mutableListOf<SingleEvent>()
    private val allGestures = mutableListOf<List<SingleEvent>>()
    private var mVelocityTracker: VelocityTracker? = null

    fun processEvent(event: MotionEvent) {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                gestureList = mutableListOf()
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)

                gestureList.add(constructEvent(event, size = event.size, time = event.eventTime))
            }
            MotionEvent.ACTION_MOVE -> {
                Timber.d("POSITION: X ${event.x} Y ${event.y}")
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    computeCurrentVelocity(1000)
                    gestureList.add(
                        constructEvent(
                            event = event,
                            size = event.size,
                            xVelocity = getXVelocity(pointerId),
                            yVelocity = getYVelocity(pointerId)
                        )
                    )
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                gestureList.add(
                    constructEvent(
                        event = event,
                        size = event.size,
                        time = event.eventTime
                    )
                )
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker?.recycle()
                mVelocityTracker = null
                allGestures.add(gestureList)
            }
        }
    }

    private fun constructEvent(
        event: MotionEvent,
        size: Float,
        xVelocity: Float = 0f,
        yVelocity: Float = 0f,
        time: Long = 0
    ) =
        SingleEvent(
            xPos = event.rawX,
            yPos = event.rawY,
            xVelocity = xVelocity,
            yVelocity = yVelocity,
            size = size,
            time = time
        )
}
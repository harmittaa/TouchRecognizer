package com.github.harmittaa.touchobserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.VelocityTracker
import com.github.harmittaa.touchobserver.model.SingleEvent
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val swipeViewModel: SwipeViewModel by viewModel()
    private var gestureList = mutableListOf<SingleEvent>()
    private val allGestures = mutableListOf<List<SingleEvent>>()
    private var mVelocityTracker: VelocityTracker? = null
    private var storeEvents = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!storeEvents) {
            return true
        }

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
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    computeCurrentVelocity(100)
                    gestureList.add(
                        constructEvent(
                            event = event,
                            size = event.size,
                            xVelocityPx100Ms = getXVelocity(pointerId),
                            yVelocityPx100Ms = getYVelocity(pointerId),
                            time = event.eventTime
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
                swipeViewModel.storeGesture(gestureList)
                allGestures.add(gestureList)
            }
        }
        return true
    }

    private fun constructEvent(
        event: MotionEvent,
        size: Float,
        xVelocityPx100Ms: Float = 0f,
        yVelocityPx100Ms: Float = 0f,
        time: Long
    ) =
        SingleEvent(
            xPos = event.rawX,
            yPos = event.rawY,
            xVelocity = xVelocityPx100Ms,
            yVelocity = yVelocityPx100Ms,
            size = size,
            time = time
        )
}


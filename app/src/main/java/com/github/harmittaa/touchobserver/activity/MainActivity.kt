package com.github.harmittaa.touchobserver.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.appcompat.app.AppCompatActivity
import com.github.harmittaa.touchobserver.R
import com.github.harmittaa.touchobserver.model.SingleEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val VELOCITY_RATE = 100
class MainActivity : AppCompatActivity() {

    private val swipeViewModel: MainActivityViewModel by viewModel()
    private var gestureList = mutableListOf<SingleEvent>()
    private var velocityTracker: VelocityTracker? = null
    var storeEvents = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }

    @SuppressLint("Recycle")
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (!storeEvents || event == null) {
            return super.dispatchTouchEvent(event)
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                gestureList = mutableListOf()
                // Reset the velocity tracker back to its initial state.
                velocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                velocityTracker = velocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                velocityTracker?.addMovement(event)

                gestureList.add(constructEvent(event, size = event.size, time = event.eventTime))
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    computeCurrentVelocity(VELOCITY_RATE)
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
                velocityTracker?.recycle()
                velocityTracker = null
                swipeViewModel.storeGesture(gestureList)
            }
        }
        return super.dispatchTouchEvent(event)
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

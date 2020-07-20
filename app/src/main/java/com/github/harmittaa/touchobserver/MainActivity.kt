package com.github.harmittaa.touchobserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.VelocityTracker
import androidx.core.view.GestureDetectorCompat
import timber.log.Timber

interface GestureSaverListener {
    fun flingGesture(ev: MotionEvent, e2: MotionEvent)
}

class GestureSaver(val listener: GestureSaverListener) : GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {
    private var hasSent = false

    override fun onShowPress(e: MotionEvent) {
        Timber.d("onShowPress")
        e.actionIndex

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        Timber.d("onSingleTapUp")
        return true
    }

    override fun onDown(e: MotionEvent): Boolean {
        Timber.d("onDown")
        return true
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        listener.flingGesture(e1, e2)
        Timber.d("onFling")
        return true
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Timber.d("onScroll")
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        Timber.d("onLongPress")
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        Timber.d("onDoubleTap")
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        Timber.d("onDoubleTapEvent")
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        Timber.d("onSingleTapConfirmed")
        return true
    }
}

class MainActivity : AppCompatActivity(), GestureSaverListener {
    private val gestureSaver = GestureSaver(this)
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gestureDetector = GestureDetectorCompat(this, gestureSaver).apply {
            setOnDoubleTapListener(gestureSaver)
        }
    }

    private var mVelocityTracker: VelocityTracker? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    computeCurrentVelocity(1000)
                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    //Timber.d("X velocity: ${getXVelocity(pointerId)}")
                    //Timber.d("Y velocity: ${getYVelocity(pointerId)}")
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
        }
        return true
    }

    fun printFling(ev1: MotionEvent, ev2: MotionEvent) {
        Timber.d("======== BEGIN NEW FLING =========")
        val startingPointerCount = ev1.pointerCount
        val endingPointerCount = ev2.pointerCount

        for (p in 0 until startingPointerCount) {
            Timber.d("HISTORY:   startingPointer %d: (%f,%f)", ev1.getPointerId(p), ev1.getX(p), ev1.getY(p))
        }

        for (p in 0 until endingPointerCount) {
            Timber.d("HISTORY:   endingPointer %d: (%f,%f)", ev2.getPointerId(p), ev2.getX(p), ev2.getY(p))
        }

        Timber.d("======== END FLING =========")
    }

    override fun flingGesture(ev: MotionEvent, e2: MotionEvent) {
        printFling(ev, e2)
    }
}


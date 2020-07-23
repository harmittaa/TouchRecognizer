package com.github.harmittaa.touchobserver

import androidx.lifecycle.*
import timber.log.Timber

const val SWIPE_THRESHOLD = 500
const val SWIPE_REQUIRED_COUNT = 10

enum class ViewStyle {
    SWIPE_DOWN_UP,
    SWIPE_UP_DOWN,
    SWIPE_LEFT_RIGHT,
    SWIPE_RIGHT_LEFT
}

class SwipeViewModel : ViewModel() {
    private val allGestures = mutableListOf<MutableList<SingleEvent>>()
    var gestureCount: MutableLiveData<Int> = MutableLiveData()
    var canContinue: MutableLiveData<Boolean> = MutableLiveData(false)


    fun storeGesture(gestureList: MutableList<SingleEvent>) {
        validateGesture(gestureList)
        if (allGestures.count() >= SWIPE_REQUIRED_COUNT) {
            canContinue.value = true
        }
    }

    private fun validateGesture(gestureList: MutableList<SingleEvent>) {
        val startPoint = gestureList.first()
        val endPoint = gestureList.last()
        if (startPoint.yPos - endPoint.yPos >= SWIPE_THRESHOLD) {
            allGestures.add(gestureList)
            gestureCount.value = allGestures.count()
            Timber.d("COUNT ${allGestures.count()}")
        }
    }
}
package com.github.harmittaa.touchobserver.screens.swipe

import androidx.lifecycle.*
import com.github.harmittaa.touchobserver.logic.GestureProcessor
import com.github.harmittaa.touchobserver.model.SingleEvent
import com.github.harmittaa.touchobserver.model.TouchGesture
import com.github.harmittaa.touchobserver.repository.TouchRepository
import timber.log.Timber

const val SWIPE_THRESHOLD = 500
const val SWIPE_REQUIRED_COUNT = 5

enum class GestureDirection {
    DOWN_UP,
    UP_DOWN,
    LEFT_RIGHT,
    RIGHT_LEFT,
    UNKNOWN
}


class SwipeViewModel(
    private val repository: TouchRepository,
    private val gestureProcessor: GestureProcessor
) : ViewModel() {
    private val allGestures = mutableListOf<TouchGesture>()
    var gestureCount: MutableLiveData<Int> = MutableLiveData()
    var canContinue: MutableLiveData<Boolean> = MutableLiveData(true)


    fun storeGesture(gestureList: MutableList<SingleEvent>) {

        gestureProcessor.validateGesture(gestureList)?.let { validGesture ->
            repository.storeGesture(validGesture)
        }
    }
}
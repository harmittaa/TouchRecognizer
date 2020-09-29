package com.github.harmittaa.touchobserver.screens.swipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.harmittaa.touchobserver.logic.GestureProcessor
import com.github.harmittaa.touchobserver.model.SingleEvent
import com.github.harmittaa.touchobserver.repository.TouchRepository

class SwipeViewModel(
    private val repository: TouchRepository,
    private val gestureProcessor: GestureProcessor
) : ViewModel() {
    var gestureCount: MutableLiveData<Int> = MutableLiveData()

    fun storeGesture(gestureList: MutableList<SingleEvent>) {
        gestureProcessor.validateGesture(gestureList)?.let { validGesture ->
            repository.storeGesture(validGesture)
        }
    }
}

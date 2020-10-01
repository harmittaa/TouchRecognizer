package com.github.harmittaa.touchobserver.activity

import androidx.lifecycle.ViewModel
import com.github.harmittaa.touchobserver.logic.GestureProcessor
import com.github.harmittaa.touchobserver.model.SingleEvent
import com.github.harmittaa.touchobserver.repository.TouchRepository

class MainActivityViewModel(
    private val repository: TouchRepository,
    private val gestureProcessor: GestureProcessor
) : ViewModel() {

    fun storeGesture(gestureList: MutableList<SingleEvent>) {
        gestureProcessor.validateGesture(gestureList)?.let { validGesture ->
            repository.storeGesture(validGesture)
        }
    }
}

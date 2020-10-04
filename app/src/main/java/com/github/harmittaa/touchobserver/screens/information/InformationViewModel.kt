package com.github.harmittaa.touchobserver.screens.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.touchobserver.repository.Resource
import com.github.harmittaa.touchobserver.repository.UserRepository
import com.github.harmittaa.touchobserver.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class InformationViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _removalRequestSuccess = MutableLiveData<Boolean>()
    val removalRequestSuccess: LiveData<Boolean> = _removalRequestSuccess
    private val _removalRequestError = SingleLiveEvent<Unit>()
    val removalRequestError: SingleLiveEvent<Unit> = _removalRequestError

    fun onDataRemovalClicked() {
        viewModelScope.launch {
            when (userRepository.onDataRemovalClicked()) {
                is Resource.Success -> {
                    _removalRequestSuccess.value = true
                }
                else -> {
                    _removalRequestSuccess.value = false
                    _removalRequestError.value = Unit
                }
            }
        }
    }
}

package com.github.harmittaa.touchobserver.screens.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.touchobserver.repository.Resource
import com.github.harmittaa.touchobserver.repository.UserRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading
    private val _initResult = MutableLiveData<Resource.Failure>()
    val initResult: LiveData<Resource.Failure> = _initResult
    private val _showNextScreen = MutableLiveData(false)
    val showNextScreen: LiveData<Boolean> = _showNextScreen

    fun onConsentGiven() {
        _showLoading.value = true
        viewModelScope.launch {
            when(val signInResult = userRepository.attemptSignIn()) {
                is Resource.Success -> instantiateUserData()
                is Resource.Failure -> {
                    _showLoading.value = false
                    _initResult.value = signInResult
                }
            }
        }
    }

    private suspend fun instantiateUserData() {
        val result = userRepository.instantiateData()
        when (result) {
            is Resource.Success -> _showNextScreen.value = true
            is Resource.Failure -> {
                _initResult.value = result
            }
        }

    }


}
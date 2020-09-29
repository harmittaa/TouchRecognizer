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
    private val _canSkipLogin = MutableLiveData(false)
    val canSkipLogin: LiveData<Boolean> = _canSkipLogin

    init {
        viewModelScope.launch {
            val consent = userRepository.hasGivenConsent()
            val userReady = userRepository.userReady()
            _canSkipLogin.value = consent && userReady
        }
    }

    fun onConsentGiven() {
        _showLoading.value = true
        viewModelScope.launch {
            when (val signInResult = userRepository.attemptSignIn()) {
                is Resource.Success -> instantiateUserData()
                is Resource.Failure -> {
                    _showLoading.value = false
                    _initResult.value = signInResult
                }
            }
        }
    }

    private suspend fun instantiateUserData() {
        when (val result = userRepository.instantiateData()) {
            is Resource.Success -> _showNextScreen.postValue(true)
            is Resource.Failure -> {
                _initResult.postValue(result)
            }
        }
    }
}

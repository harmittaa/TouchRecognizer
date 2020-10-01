package com.github.harmittaa.touchobserver.screens.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.harmittaa.touchobserver.model.UserData
import com.github.harmittaa.touchobserver.repository.Resource
import com.github.harmittaa.touchobserver.repository.UserRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    // Shared functionality
    private val _showLoading = MutableLiveData(false)
    val showLoading: LiveData<Boolean> = _showLoading
    private val _initResult = MutableLiveData<Resource.Failure>()
    val initResult: LiveData<Resource.Failure> = _initResult
    private val _showNextScreen = MutableLiveData(false)
    val showNextScreen: LiveData<Boolean> = _showNextScreen
    private val _canSkipLogin = MutableLiveData<Boolean>()
    val canSkipLogin: LiveData<Boolean> = _canSkipLogin

    private val _onContinueInvoked = MutableLiveData<Unit>()
    val onContinueInvoked: LiveData<Unit> = _onContinueInvoked

    private val _gender = MutableLiveData(UserData.Gender.MALE)
    val gender: LiveData<UserData.Gender> = _gender
    private val handedness = MutableLiveData<UserData.Handedness>()

    init {
        viewModelScope.launch {
            val consent = userRepository.hasGivenConsent()
            val userReady = userRepository.userReady()
            _canSkipLogin.value = consent && userReady
        }
    }

    fun onContinueButtonClicked() {
        _onContinueInvoked.value = Unit
    }

    fun onConsentGiven() {
        _showLoading.value = true
        viewModelScope.launch {
            when (val signInResult = userRepository.attemptSignIn()) {
                is Resource.Success -> {
                    instantiateUserData(
                        gender.value ?: UserData.Gender.MALE,
                        handedness.value ?: UserData.Handedness.RIGHT
                    )
                }
                is Resource.Failure -> {
                    _showLoading.value = false
                    _initResult.value = signInResult
                }
            }
        }
    }

    private suspend fun instantiateUserData(
        gender: UserData.Gender,
        handedness: UserData.Handedness
    ) {
        when (val result = userRepository.instantiateData(gender, handedness)) {
            is Resource.Success -> _showNextScreen.postValue(true)
            is Resource.Failure -> {
                _initResult.postValue(result)
            }
        }
    }

    // GENDER SELECTION
    private val _maleSelectedLv = MutableLiveData(false)
    val maleSelectedLv: LiveData<Boolean> = _maleSelectedLv
    private val _femaleSelectedLv = MutableLiveData(false)
    val femaleSelectedLv: LiveData<Boolean> = _femaleSelectedLv

    fun maleSelected() {
        _maleSelectedLv.value = !(_maleSelectedLv.value ?: false)
        _femaleSelectedLv.value = false
        if (_maleSelectedLv.value == true) {
            _gender.value = UserData.Gender.MALE
        }
    }

    fun femaleSelected() {
        _femaleSelectedLv.value = !(_femaleSelectedLv.value ?: false)
        _maleSelectedLv.value = false
        if (_femaleSelectedLv.value == true) {
            _gender.value = UserData.Gender.FEMALE
        }
    }

    // HANDEDNESS SELECTION
    private val _leftSelectedLv = MutableLiveData(false)
    val leftSelectedLv: LiveData<Boolean> = _leftSelectedLv
    private val _rightSelectedLv = MutableLiveData(false)
    val rightSelectedLv: LiveData<Boolean> = _rightSelectedLv

    fun leftSelected() {
        _leftSelectedLv.value = !(_leftSelectedLv.value ?: false)
        _rightSelectedLv.value = false
        if (_leftSelectedLv.value == true) {
            handedness.value = UserData.Handedness.LEFT
        }
    }

    fun rightSelected() {
        _rightSelectedLv.value = !(_rightSelectedLv.value ?: false)
        _leftSelectedLv.value = false
        if (_rightSelectedLv.value == true) {
            handedness.value = UserData.Handedness.RIGHT
        }
    }
}

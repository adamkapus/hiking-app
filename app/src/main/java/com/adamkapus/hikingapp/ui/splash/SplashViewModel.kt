package com.adamkapus.hikingapp.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.authentication.AuthenticationInteractorImpl
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Initial)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _userIsSignedInEvent = MutableStateFlow(false)
    val userIsSignedInEvent = _userIsSignedInEvent.asStateFlow()

    private val _userIsSignedOutEvent = MutableStateFlow(false)
    val userIsSignedOutEvent = _userIsSignedOutEvent.asStateFlow()

    fun checkIfUserIsSignedIn() = viewModelScope.launch {
        val inter = AuthenticationInteractorImpl()
        val response = inter.isSignedIn()
        when (response) {
            is InteractorResult -> {
                if (response.result) {
                    _userIsSignedInEvent.update { true }
                } else {
                    _userIsSignedOutEvent.update { true }
                }
            }
            is InteractorError -> {
                _userIsSignedOutEvent.update { true }
            }
        }
    }

    fun handledUserIsSignedInEvent() {
        _userIsSignedInEvent.update { false }
    }

    fun handledUserIsSignedOutEvent() {
        _userIsSignedOutEvent.update { false }
    }

}
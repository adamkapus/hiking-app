package com.adamkapus.hikingapp.ui.authentication

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AuthenticationViewModel : ViewModel(){

    private val _uiState = MutableStateFlow<AuthenticationUiState>(AuthenticationUiState.RegistrationSelected)
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    fun switchTo(authenticationScreenType: AuthenticationScreenType) {
        _uiState.value = when (authenticationScreenType) {
            AuthenticationScreenType.REGISTRATION -> {
                AuthenticationUiState.RegistrationSelected
            }
            AuthenticationScreenType.LOGIN -> {
                AuthenticationUiState.LoginSelected
            }
        }
    }
}

enum class AuthenticationScreenType {
    REGISTRATION,
    LOGIN
}
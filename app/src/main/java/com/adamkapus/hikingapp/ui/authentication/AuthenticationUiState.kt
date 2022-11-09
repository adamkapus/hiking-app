package com.adamkapus.hikingapp.ui.authentication

sealed class AuthenticationUiState {
    object Initial : AuthenticationUiState()
    object RegistrationSelected : AuthenticationUiState()
    object LoginSelected : AuthenticationUiState()
}
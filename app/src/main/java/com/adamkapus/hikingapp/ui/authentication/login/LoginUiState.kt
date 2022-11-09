package com.adamkapus.hikingapp.ui.authentication.login

sealed class LoginUiState {
    data class Initial(var isLoginButtonEnabled: Boolean) : LoginUiState()
    object LoginInProgress : LoginUiState()
}
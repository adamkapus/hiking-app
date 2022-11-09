package com.adamkapus.hikingapp.ui.authentication.registration

sealed class RegistrationUiState(){
    data class Initial(var isRegistrationButtonEnabled : Boolean) : RegistrationUiState()
    object RegistrationInProgress : RegistrationUiState()
}
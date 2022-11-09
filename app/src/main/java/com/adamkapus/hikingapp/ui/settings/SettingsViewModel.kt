package com.adamkapus.hikingapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.authentication.AuthenticationInteractorImpl
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Initial)
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _signOutSucceededEvent = MutableStateFlow(false)
    val signOutSucceededEvent = _signOutSucceededEvent.asStateFlow()

    private val _signOutFailedEvent = MutableStateFlow(false)
    val signOutFailedEvent = _signOutFailedEvent.asStateFlow()

    suspend fun loadUserInfo() = viewModelScope.launch {
        val inter = AuthenticationInteractorImpl()
        val response = inter.getUserInformation()
        when (response) {
            is InteractorResult -> {
                val user = response.result
                _uiState.update { _ -> SettingsUiState.UserInfoLoaded(user = user) }
            }
            is InteractorError -> {}
        }
    }

    suspend fun trySigningOut() = viewModelScope.launch {
        val inter = AuthenticationInteractorImpl()
        val response = inter.signOut()
        when (response) {
            is InteractorResult -> {
                if (response.result) {
                    _signOutSucceededEvent.update { true }
                } else {
                    _signOutFailedEvent.update { false }
                }
            }
            is InteractorError -> {
                _signOutFailedEvent.update { true }
            }
        }
    }

    fun handledSignOutSucceededEvent() {
        _signOutSucceededEvent.update { false }
    }

    fun handledSignOutFailedEvent() {
        _signOutFailedEvent.update { false }
    }
}
package com.adamkapus.hikingapp.ui.settings

import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser

sealed class SettingsUiState {
    object Initial : SettingsUiState()
    data class UserInfoLoaded(val user: HikingAppUser) : SettingsUiState()
}

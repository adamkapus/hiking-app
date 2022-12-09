package com.adamkapus.hikingapp.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser
import com.adamkapus.hikingapp.ui.compose.composables.PrimaryButton
import com.adamkapus.hikingapp.ui.compose.composables.PrimaryDarkButton
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppTypography
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onSuccessfulSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.hikingappColors.white),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(Unit) {
            viewModel.loadUserInfo()
        }

        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        var isUserInfoLoaded by remember { mutableStateOf(false) }
        var user: HikingAppUser? by remember { mutableStateOf(null) }

        val uiState by viewModel.uiState.collectAsState()
        when (uiState) {
            is SettingsUiState.Initial -> {}
            is SettingsUiState.UserInfoLoaded -> {
                isUserInfoLoaded = true
                user = (uiState as SettingsUiState.UserInfoLoaded).user
            }
        }

        if (isUserInfoLoaded && user != null) {
            Text(text = "Hello, " + user!!.email!! +"!", style = MaterialTheme.hikingAppTypography.infoTextStyleLarge)
            PrimaryDarkButton(
                onClickListener = {
                    coroutineScope.launch {
                        viewModel.trySigningOut()
                    }
                },
                enabled = true, text = "Sign out"
            )
        }

        val signOutSucceeded by viewModel.signOutSucceededEvent.collectAsState()
        if (signOutSucceeded) {
            LaunchedEffect(signOutSucceeded) {
                viewModel.handledSignOutSucceededEvent()
                onSuccessfulSignOut()
            }
        }

        val signOutFailed by viewModel.signOutFailedEvent.collectAsState()
        if (signOutFailed) {
            LaunchedEffect(signOutFailed) {
                viewModel.handledSignOutFailedEvent()
            }
        }

    }

}
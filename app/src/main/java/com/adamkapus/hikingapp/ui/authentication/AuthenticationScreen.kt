package com.adamkapus.hikingapp.ui.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.adamkapus.hikingapp.ui.authentication.login.LoginScreenContent
import com.adamkapus.hikingapp.ui.authentication.registration.RegistrationScreenContent
import com.adamkapus.hikingapp.ui.compose.composables.segmentedpicker.SegmentedPicker
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors
import com.adamkapus.hikingapp.utils.fromInt


@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    onSuccessfulRegistration: () -> Unit,
    onSuccessfulLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.hikingappColors.white),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val titles = listOf(
            stringResource(id = com.adamkapus.hikingapp.R.string.authentication_pager_registration),
            stringResource(id = com.adamkapus.hikingapp.R.string.authentication_pager_login)
        )

        val segments = remember { titles }
        var selectedSegment by remember { mutableStateOf(segments.first()) }

        SegmentedPicker(
            segments = segments,
            selectedSegment = selectedSegment,
            onSegmentSelected = {
                viewModel.switchTo(fromInt(segments.indexOf(it)))
            },
            modifier = Modifier
                .padding(
                    top = MaterialTheme.hikingAppDimens.gapNormal,
                    start = MaterialTheme.hikingAppDimens.gapNormal,
                    end = MaterialTheme.hikingAppDimens.gapNormal,
                    bottom = MaterialTheme.hikingAppDimens.gapNormal
                )
        )

        val uiState by viewModel.uiState.collectAsState()
        when (uiState) {
            is AuthenticationUiState.Initial -> Unit
            is AuthenticationUiState.RegistrationSelected -> {
                LaunchedEffect(uiState) {
                    selectedSegment = segments[0]
                }
                RegistrationScreenContent(
                    onSuccessfulRegistration = onSuccessfulRegistration
                )
            }
            is AuthenticationUiState.LoginSelected -> {
                LaunchedEffect(uiState) {
                    selectedSegment = segments[1]
                }
                LoginScreenContent(
                    onSuccessfulLogin = onSuccessfulLogin
                )
            }
        }
    }
}



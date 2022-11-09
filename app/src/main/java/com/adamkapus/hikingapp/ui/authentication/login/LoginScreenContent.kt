package com.adamkapus.hikingapp.ui.authentication.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.ui.compose.composables.EmailTextField
import com.adamkapus.hikingapp.ui.compose.composables.ItemGroup
import com.adamkapus.hikingapp.ui.compose.composables.PrimaryButton
import com.adamkapus.hikingapp.ui.compose.composables.textfield.PasswordTextField
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppTypography
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors
import kotlinx.coroutines.launch

@Composable
fun LoginScreenContent(
    viewModel: LoginViewModel = hiltViewModel(),
    onSuccessfulLogin: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()

    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginButtonEnabled by remember { mutableStateOf(false) }

    var isLoginInProgress by remember { mutableStateOf(false) }


    when (uiState) {
        is LoginUiState.Initial -> {
            LaunchedEffect(uiState) {
                isLoginButtonEnabled =
                    (uiState as LoginUiState.Initial).isLoginButtonEnabled
                isLoginInProgress = false
            }
        }
        is LoginUiState.LoginInProgress -> {
            LaunchedEffect(uiState) {
                isLoginInProgress = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.hikingappColors.grey01),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ItemGroup(
            itemList = listOf {
                EmailTextField(
                    email = emailAddress,
                    onEmailTextChangeListener = {
                        emailAddress = it
                        viewModel.checkIfFormValid(
                            emailAddress,
                            password
                        )
                    }
                )
            },
            modifier = Modifier
                .padding(
                    top = MaterialTheme.hikingAppDimens.gapNormal,
                    start = MaterialTheme.hikingAppDimens.gapNormal,
                    end = MaterialTheme.hikingAppDimens.gapNormal
                )
        )
        ItemGroup(
            itemList = listOf {
                PasswordTextField(
                    password = password,
                    onPasswordTextChangeListener = {
                        password = it
                        viewModel.checkIfFormValid(
                            emailAddress,
                            password
                        )
                    },
                )
            },
            modifier = Modifier
                .padding(
                    top = MaterialTheme.hikingAppDimens.gapNormal,
                    start = MaterialTheme.hikingAppDimens.gapNormal,
                    end = MaterialTheme.hikingAppDimens.gapNormal
                )
        )
        Text(
            text = stringResource(id = R.string.registration_labels_password_hint),
            style = MaterialTheme.hikingAppTypography.infoTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MaterialTheme.hikingAppDimens.gapNormal,
                    start = MaterialTheme.hikingAppDimens.gapNormal,
                    end = MaterialTheme.hikingAppDimens.gapNormal
                )
        )
        PrimaryButton(
            onClickListener = {
                coroutineScope.launch {
                    viewModel.trySignIn(
                        emailAddress,
                        password
                    )
                }
            },
            enabled = isLoginButtonEnabled,
            text = stringResource(id = com.adamkapus.hikingapp.R.string.login_buttons_next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MaterialTheme.hikingAppDimens.gapVeryLarge,
                    bottom = MaterialTheme.hikingAppDimens.gapNormal,
                    start = MaterialTheme.hikingAppDimens.gapNormal,
                    end = MaterialTheme.hikingAppDimens.gapNormal
                )
        )

        val loginFormIncorrect by viewModel.loginFormIncorrectEvent.collectAsState()
        if (loginFormIncorrect.isLoginFormIncorrect) {
            LaunchedEffect(loginFormIncorrect.isLoginFormIncorrect) {
                viewModel.handledLoginFormIncorrectEvent()
                Toast.makeText(context, "Hibas form", Toast.LENGTH_SHORT).show()
            }
        }

        val loginSucceeded by viewModel.loginSucceededEvent.collectAsState()
        if (loginSucceeded) {
            LaunchedEffect(loginSucceeded) {
                viewModel.handledLoginSucceededEvent()
                onSuccessfulLogin()
            }
        }

        val loginFailed by viewModel.loginFailedEvent.collectAsState()
        if (loginFailed) {
            LaunchedEffect(loginFailed) {
                viewModel.handledLoginFailedEvent()
                Toast.makeText(context, "reg hiba", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
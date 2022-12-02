package com.adamkapus.hikingapp.ui.authentication.registration

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
import com.adamkapus.hikingapp.ui.compose.composables.EmailTextField
import com.adamkapus.hikingapp.ui.compose.composables.ItemGroup
import com.adamkapus.hikingapp.ui.compose.composables.PrimaryButton
import com.adamkapus.hikingapp.ui.compose.composables.textfield.PasswordTextField
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppTypography
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreenContent(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onSuccessfulRegistration: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()

    var emailAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordAgain by remember { mutableStateOf("") }
    var isRegistrationButtonEnabled by remember { mutableStateOf(false) }

    var isRegistrationInProgress by remember { mutableStateOf(false) }


    when (uiState) {
        is RegistrationUiState.Initial -> {
            LaunchedEffect(uiState) {
                isRegistrationButtonEnabled =
                    (uiState as RegistrationUiState.Initial).isRegistrationButtonEnabled
                isRegistrationInProgress = false
            }
        }
        is RegistrationUiState.RegistrationInProgress -> {
            LaunchedEffect(uiState) {
                isRegistrationInProgress = true
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
                            password,
                            passwordAgain
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
                            password,
                            passwordAgain
                        )
                    },
                )
                PasswordTextField(
                    password = passwordAgain,
                    onPasswordTextChangeListener = {
                        passwordAgain = it
                        viewModel.checkIfFormValid(
                            emailAddress,
                            password,
                            passwordAgain
                        )
                    },
                    label = com.adamkapus.hikingapp.R.string.registration_inputs_password_again,
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
            text = stringResource(id = com.adamkapus.hikingapp.R.string.login_labels_password_hint),
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
                    viewModel.tryRegistration(
                        emailAddress,
                        password,
                        passwordAgain
                    )
                }
            },
            enabled = isRegistrationButtonEnabled,
            text = stringResource(id = com.adamkapus.hikingapp.R.string.registration_buttons_next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MaterialTheme.hikingAppDimens.gapVeryLarge,
                    bottom = MaterialTheme.hikingAppDimens.gapNormal,
                    start = MaterialTheme.hikingAppDimens.gapNormal,
                    end = MaterialTheme.hikingAppDimens.gapNormal
                )
        )

        val registrationFormIncorrectEvent by viewModel.registrationFormIncorrectEvent.collectAsState()
        if (registrationFormIncorrectEvent.isRegistrationFormIncorrect) {
            LaunchedEffect(registrationFormIncorrectEvent.isRegistrationFormIncorrect) {
                viewModel.handledRegistrationFormIncorrectEvent()
                Toast.makeText(context, "Hibas form", Toast.LENGTH_SHORT).show()
            }
        }

        val registrationSucceeded by viewModel.registrationSucceededEvent.collectAsState()
        if (registrationSucceeded) {
            LaunchedEffect(registrationSucceeded) {
                viewModel.handledRegistrationSucceededEvent()
                onSuccessfulRegistration()
            }
        }

        val registrationFailed by viewModel.registrationFailedEvent.collectAsState()
        if (registrationFailed) {
            LaunchedEffect(registrationFailed) {
                viewModel.handledRegistrationFailedEvent()
                Toast.makeText(context, "reg hiba", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
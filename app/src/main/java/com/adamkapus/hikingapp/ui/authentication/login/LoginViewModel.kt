package com.adamkapus.hikingapp.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.authentication.AuthenticationInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.ui.authentication.registration.AuthenticationFailureType
import com.adamkapus.hikingapp.utils.isNotNullOrBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LoginUiState>(
            LoginUiState.Initial(
                isLoginButtonEnabled = false
            )
        )
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginFormIncorrectEvent = MutableStateFlow(
        LoginFormIncorrect(
            false,
            listOf()
        )
    )
    val loginFormIncorrectEvent = _loginFormIncorrectEvent.asStateFlow()

    private val _loginSucceededEvent = MutableStateFlow(
        false
    )
    val loginSucceededEvent = _loginSucceededEvent.asStateFlow()

    private val _loginFailedEvent = MutableStateFlow(
        false
    )
    val loginFailedEvent = _loginFailedEvent.asStateFlow()

    companion object {
        private const val EMAIL_REGEX =
            "[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})"
        private const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$"
    }


    fun checkIfFormValid(
        emailAddress: String?,
        password: String?,
    ) {
        val isFormValid = emailAddress.isNotNullOrBlank() &&
                password.isNotNullOrBlank()
        _uiState.update { _ -> LoginUiState.Initial(isLoginButtonEnabled = isFormValid) }
    }


    suspend fun trySignIn(
        emailAddress: String,
        password: String
    ) {
        val errorList = ArrayList<AuthenticationFailureType>()
        if (emailAddress.isBlank() || !EMAIL_REGEX.toRegex().matches(emailAddress)) {
            errorList.add(AuthenticationFailureType.INVALID_EMAIL_FORMAT)
        }

        if (!PASSWORD_REGEX.toRegex().matches(password)) {
            errorList.add(AuthenticationFailureType.TOO_SHORT_PASSWORD)
        }

        when (errorList) {
            emptyList<AuthenticationFailureType>() -> {
                login(emailAddress, password)
            }

            else -> _loginFormIncorrectEvent.update {
                LoginFormIncorrect(
                    true,
                    errorList
                )
            }
        }
    }

    private suspend fun login(emailAddress: String, password: String) = viewModelScope.launch {
        _uiState.update { _ -> LoginUiState.LoginInProgress }
        val response = authenticationInteractor.signIn(emailAddress, password)
        when (response) {
            is InteractorResult -> {
                val success = response.result
                if (success) {
                    _loginSucceededEvent.update { true }
                } else {
                    _uiState.update { _ -> LoginUiState.Initial(isLoginButtonEnabled = true) }
                    _loginFailedEvent.update { true }
                }
            }
            is InteractorError -> {}
        }
    }

    fun handledLoginFormIncorrectEvent() {
        _loginFormIncorrectEvent.update {
            LoginFormIncorrect(
                isLoginFormIncorrect = false,
                failureType = listOf()
            )
        }
    }

    fun handledLoginSucceededEvent() {
        _loginSucceededEvent.update { false }
    }

    fun handledLoginFailedEvent() {
        _loginFailedEvent.update { false }
    }

}

data class LoginFormIncorrect(
    val isLoginFormIncorrect: Boolean,
    val failureType: List<AuthenticationFailureType>
)
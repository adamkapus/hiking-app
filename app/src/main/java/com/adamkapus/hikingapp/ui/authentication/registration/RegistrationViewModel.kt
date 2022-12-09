package com.adamkapus.hikingapp.ui.authentication.registration

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.domain.interactor.authentication.AuthenticationInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.utils.isNotNullOrBlank
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<RegistrationUiState>(
            RegistrationUiState.Initial(
                isRegistrationButtonEnabled = false
            )
        )
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _registrationFormIncorrectEvent = MutableStateFlow(
        RegistrationFormIncorrect(
            false,
            listOf()
        )
    )
    val registrationFormIncorrectEvent = _registrationFormIncorrectEvent.asStateFlow()

    private val _registrationSucceededEvent = MutableStateFlow(
        false
    )
    val registrationSucceededEvent = _registrationSucceededEvent.asStateFlow()

    private val _registrationFailedEvent = MutableStateFlow(
        false
    )
    val registrationFailedEvent = _registrationFailedEvent.asStateFlow()

    companion object {
        private const val EMAIL_REGEX =
            "[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})"
        private const val PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}\$"
    }


    fun checkIfFormValid(
        emailAddress: String?,
        password: String?,
        passwordAgain: String?
    ) {
        val isFormValid = emailAddress.isNotNullOrBlank() &&
                password.isNotNullOrBlank() &&
                passwordAgain.isNotNullOrBlank()
        _uiState.update { _ -> RegistrationUiState.Initial(isRegistrationButtonEnabled = isFormValid) }
    }


    suspend fun tryRegistration(
        emailAddress: String,
        password: String,
        passwordAgain: String
    ) {
        val errorList = ArrayList<AuthenticationFailureType>()
        if (emailAddress.isBlank() || !EMAIL_REGEX.toRegex().matches(emailAddress)) {
            errorList.add(AuthenticationFailureType.INVALID_EMAIL_FORMAT)
        }

        if (password.isBlank() || passwordAgain.isBlank() || password != passwordAgain) {
            errorList.add(AuthenticationFailureType.MISMATCHED_PASSWORD)
        }

        if (!PASSWORD_REGEX.toRegex().matches(password)) {
            errorList.add(AuthenticationFailureType.TOO_SHORT_PASSWORD)
        }

        when (errorList) {
            emptyList<AuthenticationFailureType>() -> {
                register(emailAddress, password)
            }

            else -> _registrationFormIncorrectEvent.update {
                RegistrationFormIncorrect(
                    true,
                    errorList
                )
            }
        }
    }

    private suspend fun register(emailAddress: String, password: String) = viewModelScope.launch {
        _uiState.update { _ -> RegistrationUiState.RegistrationInProgress }
        val response = authenticationInteractor.signUp(emailAddress, password)
        when (response) {
            is InteractorResult -> {
                val success = response.result
                if (success) {
                    _registrationSucceededEvent.update { true }
                } else {
                    _uiState.update { _ -> RegistrationUiState.Initial(isRegistrationButtonEnabled = true) }
                    _registrationFailedEvent.update { true }
                }
            }
            is InteractorError -> {}
        }
    }

    fun handledRegistrationFormIncorrectEvent() {
        _registrationFormIncorrectEvent.update {
            RegistrationFormIncorrect(
                isRegistrationFormIncorrect = false,
                failureType = listOf()
            )
        }
    }

    fun handledRegistrationSucceededEvent() {
        _registrationSucceededEvent.update { false }
    }

    fun handledRegistrationFailedEvent() {
        _registrationFailedEvent.update { false }
    }
}

data class RegistrationFormIncorrect(
    val isRegistrationFormIncorrect: Boolean,
    val failureType: List<AuthenticationFailureType>
)

enum class AuthenticationFailureType(@StringRes open val titleRes: Int) {
    INVALID_EMAIL_FORMAT(R.string.authentication_failure_alert_invalid_email_format),
    TOO_SHORT_PASSWORD(R.string.authentication_failure_alert_too_short_password),
    MISMATCHED_PASSWORD(R.string.authentication_failure_alert_unmatching_passwords),
}
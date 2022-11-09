package com.adamkapus.hikingapp.ui.compose.composables.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.ui.compose.composables.HikingappTextField

@Composable
fun PasswordTextField(
    password: String,
    onPasswordTextChangeListener: (String) -> Unit,
    label: Int = R.string.login_inputs_password,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    HikingappTextField(
        text = password,
        onTextChangeListener = onPasswordTextChangeListener,
        label = label,
        leadingIcon = R.drawable.ic_password,
        isPassword = true,
        passwordVisible = passwordVisible,
        onPasswordVisibilityChangeListener = { passwordVisible = !passwordVisible },
        keyBoardType = KeyboardType.Password
    )
}
package com.adamkapus.hikingapp.ui.compose.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.adamkapus.hikingapp.R

@Composable
fun EmailTextField(email: String, onEmailTextChangeListener: (String) -> Unit) {

    HikingappTextField(
        text = email,
        onTextChangeListener = onEmailTextChangeListener,
        label = R.string.login_inputs_email,
        leadingIcon = R.drawable.ic_email,
        keyBoardType = KeyboardType.Email
    )
}
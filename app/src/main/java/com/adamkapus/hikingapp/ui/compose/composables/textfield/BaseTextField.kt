package com.adamkapus.hikingapp.ui.compose.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppTypography
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors

@Composable
fun HikingappTextField(
    text: String,
    onTextChangeListener: (String) -> Unit,
    label: Int,
    leadingIcon: Int,
    keyBoardType: KeyboardType,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChangeListener: () -> Unit = {}
) {

    val hikingAppTextFieldColors = TextFieldDefaults.textFieldColors(
        leadingIconColor = MaterialTheme.hikingappColors.primaryTextColor,
        trailingIconColor = MaterialTheme.hikingappColors.primaryTextColor,
        focusedLabelColor = MaterialTheme.hikingappColors.primaryTextColor,
        textColor = MaterialTheme.hikingappColors.primaryTextColor,
        backgroundColor = MaterialTheme.hikingappColors.transparent,
        focusedIndicatorColor = MaterialTheme.hikingappColors.transparent,
        unfocusedIndicatorColor = MaterialTheme.hikingappColors.transparent,
        disabledIndicatorColor = MaterialTheme.hikingappColors.transparent,
        cursorColor = MaterialTheme.hikingappColors.primaryColor
    )
    if (!isPassword) {
        TextField(
            value = text,
            onValueChange = onTextChangeListener,
            singleLine = true,
            label = { Text(stringResource(label)) },
            leadingIcon = { Icon(painter = painterResource(id = leadingIcon), contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(MaterialTheme.hikingAppDimens.inputMinHeight),
            colors = hikingAppTextFieldColors,
            textStyle = MaterialTheme.hikingAppTypography.hikingAppTextFieldTextStyle,
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType)
        )
    } else {
        TextField(
            value = text,
            onValueChange = onTextChangeListener,
            singleLine = true,
            label = { Text(stringResource(label)) },
            leadingIcon = { Icon(painter = painterResource(id = leadingIcon), contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(MaterialTheme.hikingAppDimens.inputMinHeight),
            colors = hikingAppTextFieldColors,
            textStyle = MaterialTheme.hikingAppTypography.hikingAppTextFieldTextStyle,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
            trailingIcon = {
                val image: Painter =
                    if (passwordVisible) {
                        painterResource(id = com.adamkapus.hikingapp.R.drawable.ic_visibilty)
                    } else {
                        painterResource(id = com.adamkapus.hikingapp.R.drawable.ic_visibilty)
                    }

                IconButton(onClick = onPasswordVisibilityChangeListener) {
                    Icon(painter = image, contentDescription = null)
                }
            }
        )
    }
}
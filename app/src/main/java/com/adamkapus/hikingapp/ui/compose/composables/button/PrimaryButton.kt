package com.adamkapus.hikingapp.ui.compose.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppTypography
import com.adamkapus.hikingapp.ui.compose.theme.hikingappColors

@Composable
fun PrimaryButton(
    onClickListener: () -> Unit,
    enabled: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    BaseButton(
        onClickListener = onClickListener,
        modifier = modifier.padding(
            start = MaterialTheme.hikingAppDimens.gapNormal,
            end = MaterialTheme.hikingAppDimens.gapNormal
        ),
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.hikingappColors.primaryColor,
            disabledBackgroundColor = MaterialTheme.hikingappColors.primaryDarkColor
        ),
        shape = RoundedCornerShape(MaterialTheme.hikingAppDimens.primaryButtonCornerSize),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.hikingAppTypography.primaryButtonTextStyle
        )
    }
}

@Composable
fun PrimaryDarkButton(
    onClickListener: () -> Unit,
    enabled: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    BaseButton(
        onClickListener = onClickListener,
        modifier = modifier.padding(
            start = MaterialTheme.hikingAppDimens.gapNormal,
            end = MaterialTheme.hikingAppDimens.gapNormal
        ),
        buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.hikingappColors.primaryDarkColor,
            disabledBackgroundColor = MaterialTheme.hikingappColors.primaryDarkColor
        ),
        shape = RoundedCornerShape(MaterialTheme.hikingAppDimens.primaryButtonCornerSize),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.hikingAppTypography.primaryDarkButtonTextStyle
        )
    }
}

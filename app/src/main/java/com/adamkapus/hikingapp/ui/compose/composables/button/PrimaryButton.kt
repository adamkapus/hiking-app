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
            backgroundColor = MaterialTheme.hikingappColors.blue,
            disabledBackgroundColor = MaterialTheme.hikingappColors.midBlue
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
fun PrimaryWhiteButton(
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
            backgroundColor = MaterialTheme.hikingappColors.white,
            disabledBackgroundColor = MaterialTheme.hikingappColors.white
        ),
        shape = RoundedCornerShape(MaterialTheme.hikingAppDimens.primaryButtonCornerSize),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.hikingAppTypography.primaryWhiteButtonTextStyle
        )
    }
}

@Preview(name = "PrimaryButton enabled", showBackground = true)
@Composable
fun PrimaryButtonEnabledPreview() {
    PrimaryButton(onClickListener = {}, enabled = true, text = "Short")
}

@Preview(name = "PrimaryButton disabled", showBackground = true)
@Composable
fun PrimaryButtonDisabledPreview() {
    PrimaryButton(onClickListener = {}, enabled = false, text = "Loooong text")
}

@Preview(name = "PrimaryWhiteButton enabled", showBackground = true)
@Composable
fun PrimaryWhiteButtonEnabledPreview() {
    Surface(color = Color.Black) {
        PrimaryWhiteButton(onClickListener = {}, enabled = true, text = "Short")
    }
}

@Preview(name = "PrimaryWhiteButton disabled", showBackground = true)
@Composable
fun PrimaryWhiteButtonDisabledPreview() {
    Surface(color = Color.Black) {
        PrimaryWhiteButton(onClickListener = {}, enabled = false, text = "Loooong text")
    }
}
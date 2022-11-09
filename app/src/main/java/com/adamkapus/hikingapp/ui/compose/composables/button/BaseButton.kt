package com.adamkapus.hikingapp.ui.compose.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppDimens

@Composable
fun BaseButton(
    onClickListener: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClickListener,
        modifier = modifier.heightIn(min = MaterialTheme.hikingAppDimens.minButtonHeight),
        colors = buttonColors,
        enabled = enabled,
        shape = shape,
        elevation = elevation,
        contentPadding = contentPadding,
        content = content
    )
}
package com.adamkapus.hikingapp.ui.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun HikingAppTheme(content: @Composable () -> Unit) {

    CompositionLocalProvider(
        LocalColors provides HikingAppColors(),
        LocalTypography provides HikingAppTypography(),
        LocalDimens provides HikingAppDimens()
    ) {
        MaterialTheme(
            colors = LocalColors.current.material,
            typography = MaterialTheme.typography,
            shapes = Shapes,
            content = content
        )
    }
}
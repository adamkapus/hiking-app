package com.adamkapus.hikingapp.ui.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

data class HikingAppDimens(

    val minButtonHeight: Dp = 40.dp,

    val gapVeryTiny: Dp = 1.dp,
    val gapTiny: Dp = 2.dp,
    val gapSmall: Dp = 4.dp,
    val gapMedium: Dp = 8.dp,
    val gapNormal: Dp = 16.dp,
    val gapLarge: Dp = 24.dp,
    val gapVeryLarge: Dp = 32.dp,
    val gapVeryVeryLarge: Dp = 56.dp,
    val gapExtraLarge: Dp = 64.dp,

    val dividerHeight: Dp = 1.dp,

    val inputMinHeight: Dp = 60.dp,

    val primaryButtonCornerSize: Dp = 20.dp,

    val itemGroupCornerSize: Dp = 8.dp

)

val LocalDimens = staticCompositionLocalOf { HikingAppDimens() }

val MaterialTheme.hikingAppDimens: HikingAppDimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current
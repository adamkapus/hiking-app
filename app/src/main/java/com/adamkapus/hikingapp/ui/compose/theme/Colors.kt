package com.adamkapus.hikingapp.ui.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HikingAppColors(

    // General
    val transparent: Color = Color(0x00000000),
    val black: Color = Color(0xFF000000),
    val white: Color = Color(0xFFFFFFFF),

    // Skinsolute
    val fakeBlack: Color = Color(0xFF1E1E22),
    val pink: Color = Color(0xFF93328E),
    val midPink: Color = Color(0xFFC898C6),
    val halfPink: Color = Color(0xFF5093328E),
    val lightPink: Color = Color(0xFFEEDEED),
    val darkBlue: Color = Color(0xFF003569),
    val blue: Color = Color(0xFF004990),
    val blue25: Color = Color(0xFF40004990),
    val midBlue: Color = Color(0xFF5C8AB8),
    val lightBlue: Color = Color(0xFFD6E2ED),
    val cyan: Color = Color(0xFF64C3D5),
    val cyanMid: Color = Color(0xFF9CD9E4),
    val cyanLight: Color = Color(0xFFE6F5F8),
    val green: Color = Color(0xFF7EC29E),
    val midGreen: Color = Color(0xFFADD8C1),
    val lightGreen: Color = Color(0xFFEAF5EF),
    val orange: Color = Color(0xFFE8B67E),
    val midOrange: Color = Color(0xFFF1D1AD),
    val lightOrange: Color = Color(0xFFFBF3EA),
    val red: Color = Color(0xFFD53E44),
    val redMid: Color = Color(0xFFEA9FA1),
    val redLight: Color = Color(0xFFF8E0E1),

    val pollAnswerPercentageSelected: Color = midPink,

    val grey01: Color = Color(0xFFF0F0F4),
    val grey02: Color = Color(0xFFE1E1E5),
    val grey03: Color = Color(0xFFB4B4B8),
    val grey04: Color = Color(0xFF78787C),
    val grey04Selector: Color = Color(0xFF4278787C),

    val searchPlaceholderTextColor: Color = Color(0xFFC3C3C7),

    val material: Colors = lightColors(
        primary = pink,
        primaryVariant = pink,
        onPrimary = white,
        secondary = grey01,
        secondaryVariant = grey01,
        onSecondary = fakeBlack,
    )
)

val LocalColors = staticCompositionLocalOf { HikingAppColors() }

val MaterialTheme.hikingappColors: HikingAppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current
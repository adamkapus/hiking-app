package com.adamkapus.hikingapp.ui.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class HikingAppColors(

    val transparent: Color = Color(0x00000000),
    val black: Color = Color(0xFF000000),
    val white: Color = Color(0xFFFFFFFF),


    // General
    val primaryColor: Color = Color(0xff80deea),
    val primaryLightColor: Color = Color(0xffb4ffff),
    val primaryDarkColor: Color = Color(0xff4bacb8),
    val secondaryColor: Color = Color(0xff66bb6a),
    val secondaryLightColor: Color = Color(0xff98ee99),
    val secondaryDarkColor: Color = Color(0xff338a3e),
    val primaryTextColor: Color = Color(0xff000000),
    val secondaryTextColor: Color = Color(0xff000000),


    val material: Colors = lightColors(
        primary = primaryColor,
        primaryVariant = primaryDarkColor,
        onPrimary = primaryTextColor,
        secondary = secondaryColor,
        secondaryVariant = secondaryDarkColor,
        onSecondary = secondaryTextColor,
    )
)

val LocalColors = staticCompositionLocalOf { HikingAppColors() }

val MaterialTheme.hikingappColors: HikingAppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current
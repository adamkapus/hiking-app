package com.adamkapus.hikingapp.ui.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

data class HikingAppTypography(

    val segmentedPickerTextStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Medium
    ),
    val hikingAppTextFieldTextStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    val primaryButtonTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = HikingAppColors().primaryTextColor,
        letterSpacing = 0.sp
    ),
    val primaryDarkButtonTextStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = HikingAppColors().primaryTextColor,
        letterSpacing = 0.sp
    ),
    val infoTextStyle: TextStyle = TextStyle(
        fontSize = 14.sp,
        color = HikingAppColors().primaryTextColor,
        textAlign = TextAlign.Center
    ),
    val infoTextStyleLarge: TextStyle = TextStyle(
        fontSize = 25.sp,
        color = HikingAppColors().primaryTextColor,
        textAlign = TextAlign.Center
    ),

    )

val LocalTypography = staticCompositionLocalOf { HikingAppTypography() }

val MaterialTheme.hikingAppTypography: HikingAppTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalTypography.current
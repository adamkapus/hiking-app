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

    val dashboardTabbarHeight: Dp = 72.dp,

    val minSkinsoluteTextFieldHeight: Dp = 72.dp,

    val inputMinHeight: Dp = 56.dp,

    val primaryButtonCornerSize: Dp = 20.dp,
    val socialButtonCornerSize: Dp = 20.dp,

    /** Padding inside the track. */
    val segmentedPickerTrackPadding: Dp = 4.dp,
    /** Additional padding to inset segments and the thumb when pressed. */
    val segmentedPickerPressedTrackPadding: Dp = 2.dp,
    /** Padding inside individual segments. */
    val segmentedPickerSegmentPadding: Dp = 5.dp,
    val segmentedPickerCornerSize: Dp = 8.dp,
    val segmentedPickerThumbCornerSize: Dp = 4.dp,

    val skinsoluteCheckboxSize: Dp = 24.dp,

    //Numbers
    val minOnboardingScreenDescriptionTextLineNum: Int = 4,
    //val onboardingScreenDescriptionTextLineHeight: TextUnit = HikingAppTypography().onBoardingScreenDescriptionTextStyle.fontSize * 4 / 3,
    //val minOnboardingScreenDescriptionTextLineHeight: TextUnit = onboardingScreenDescriptionTextLineHeight * minOnboardingScreenDescriptionTextLineNum,

    val maxVerificationCodeLength: Int = 6,

    val minPasswordLength: Int = 8,

    val modalCornerSize: Dp = 8.dp,

    val pinPadButtonCornerSize: Dp = 8.dp,

    val loadingDialogHeight: Dp = 104.dp,

    val itemGroupCornerSize: Dp = 8.dp
)

val LocalDimens = staticCompositionLocalOf { HikingAppDimens() }

val MaterialTheme.hikingAppDimens: HikingAppDimens
    @Composable
    @ReadOnlyComposable
    get() = LocalDimens.current
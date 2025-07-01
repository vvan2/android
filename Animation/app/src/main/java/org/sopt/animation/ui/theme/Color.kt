package org.sopt.animation.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Immutable


// Add color
val redPrimary = Color(0xFFEA2741)
val redSecondary = Color(0xFF990015)
val redThird = Color(0xFFFFA8B4)

val bluePrimary = Color(0xFF01BBE9)
val blueSecondary = Color(0xFF4494AE)
val blueThird = Color(0xFF13AAC9)

val cloud = Color(0xFFBDCFED)
val rain = Color(0xFF747E8F)
val sun1 = Color(0xFFF1BAC2)
val sun2 = Color(0xFFF7DC81)

val grey100 = Color(0xFFF9FCFF)
val grey200 = Color(0xFFB6BECB)
val grey300 = Color(0xFFB0B4BD)
val grey400 = Color(0xFF878D99)
val grey600 = Color(0xFF3C3F48)
val grey700 = Color(0xFF2F323B)
val grey800 = Color(0xFF1C1F26)
val grey900 = Color(0xFF111318)

val white = Color(0xFFFFFFFF)
val black = Color(0xFF000000)

@Immutable
data class alarmiColors(
    val redPrimary: Color,
    val redSecondary: Color,
    val redThird: Color,

    val bluePrimary: Color,
    val blueSecondary: Color,
    val blueThird: Color,

    val cloud: Color,
    val rain: Color,
    val sun1: Color,
    val sun2: Color,

    val grey100: Color,
    val grey200: Color,
    val grey300: Color,
    val grey400: Color,
    val grey600: Color,
    val grey700: Color,
    val grey800: Color,
    val grey900: Color,

    val white: Color,
    val black: Color
)

val defaultAlarmiColors = alarmiColors(
    redPrimary = redPrimary,
    redSecondary = redSecondary,
    redThird = redThird,

    bluePrimary = bluePrimary,
    blueSecondary = blueSecondary,
    blueThird = blueThird,

    cloud = cloud,
    rain = rain,
    sun1 = sun1,
    sun2 = sun2,

    grey100 = grey100,
    grey200 = grey200,
    grey300 = grey300,
    grey400 = grey400,
    grey600 = grey600,
    grey700 = grey700,
    grey800 = grey800,
    grey900 = grey900,

    white = white,
    black = black
)
package com.example.newsapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Primary Colors
val PrimaryBlue = Color(0xFF1976D2)
val PrimaryDarkBlue = Color(0xFF1565C0)
val PrimaryLightBlue = Color(0xFF42A5F5)
val PrimaryBlueLight = Color(0xFF64B5F6)

// Accent Colors
val AccentOrange = Color(0xFFFF9800)
val AccentOrangeLight = Color(0xFFFFB74D)

// Background Colors
val BackgroundLight = Color(0xFFF5F5F5)
val BackgroundDark = Color(0xFF121212)
val SurfaceWhite = Color(0xFFFFFFFF)
val SurfaceDark = Color(0xFF1E1E1E)

// Text Colors
val TextPrimary = Color(0xFF212121)
val TextSecondary = Color(0xFF757575)
val TextTertiary = Color(0xFF9E9E9E)

// State Colors
val ErrorRed = Color(0xFFD32F2F)
val SuccessGreen = Color(0xFF388E3C)
val WarningYellow = Color(0xFFFFA000)

// Favorite Colors
val FavoriteRed = Color(0xFFE91E63)
val FavoriteRedLight = Color(0xFFF48FB1)
val FavoriteRedDark = Color(0xFFC2185B)

// Shimmer Colors
val ShimmerLight = Color(0xFFE0E0E0)
val ShimmerHighlight = Color(0xFFF5F5F5)
val ShimmerDark = Color(0xFF424242)
val ShimmerDarkHighlight = Color(0xFF616161)

// Card Colors
val CardShadow = Color(0x1A000000)
val CardOverlay = Color(0x80000000)
val CardOverlayLight = Color(0x40000000)

// Gradient Color Pairs
val GradientBlueStart = Color(0xFF1976D2)
val GradientBlueEnd = Color(0xFF1565C0)
val GradientRedStart = Color(0xFFE91E63)
val GradientRedEnd = Color(0xFFC2185B)

/**
 * Predefined gradient brushes for premium UI effects
 */
object AppGradients {

    val primaryGradient = Brush.verticalGradient(
        colors = listOf(PrimaryBlue, PrimaryDarkBlue)
    )

    val primaryHorizontalGradient = Brush.horizontalGradient(
        colors = listOf(PrimaryBlue, PrimaryDarkBlue)
    )

    val favoriteGradient = Brush.verticalGradient(
        colors = listOf(FavoriteRed, FavoriteRedDark)
    )

    val favoriteHorizontalGradient = Brush.horizontalGradient(
        colors = listOf(FavoriteRedLight, FavoriteRed)
    )

    val shimmerGradientLight = Brush.horizontalGradient(
        colors = listOf(
            ShimmerLight,
            ShimmerHighlight,
            ShimmerLight
        )
    )

    val shimmerGradientDark = Brush.horizontalGradient(
        colors = listOf(
            ShimmerDark,
            ShimmerDarkHighlight,
            ShimmerDark
        )
    )

    val cardOverlayGradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            CardOverlayLight,
            CardOverlay
        )
    )

    val cardOverlayGradientTop = Brush.verticalGradient(
        colors = listOf(
            CardOverlay,
            CardOverlayLight,
            Color.Transparent
        )
    )

    val heroImageGradient = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color.Transparent,
            Color.Black.copy(alpha = 0.3f),
            Color.Black.copy(alpha = 0.7f)
        )
    )

    fun shimmerGradient(isDark: Boolean) = if (isDark) shimmerGradientDark else shimmerGradientLight
}

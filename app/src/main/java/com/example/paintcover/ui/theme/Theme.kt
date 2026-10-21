package com.example.paintcover.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.paintcover.data.model.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = PaintPrimaryDark,
    onPrimary = PaintOnPrimaryDark,
    primaryContainer = PaintPrimaryContainerDark,
    onPrimaryContainer = PaintOnPrimaryContainerDark,
    secondary = PaintSecondaryDark,
    onSecondary = PaintOnSecondaryDark,
    secondaryContainer = PaintSecondaryContainerDark,
    onSecondaryContainer = PaintOnSecondaryContainerDark,
    tertiary = PaintTertiaryDark,
    onTertiary = PaintOnTertiaryDark,
    tertiaryContainer = PaintTertiaryContainerDark,
    onTertiaryContainer = PaintOnTertiaryContainerDark,
    background = PaintBackgroundDark,
    onBackground = PaintOnBackgroundDark,
    surface = PaintSurfaceDark,
    onSurface = PaintOnSurfaceDark,
    surfaceVariant = PaintSurfaceVariantDark,
    onSurfaceVariant = PaintOnSurfaceVariantDark,
    outline = PaintOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = PaintPrimaryLight,
    onPrimary = PaintOnPrimaryLight,
    primaryContainer = PaintPrimaryContainerLight,
    onPrimaryContainer = PaintOnPrimaryContainerLight,
    secondary = PaintSecondaryLight,
    onSecondary = PaintOnSecondaryLight,
    secondaryContainer = PaintSecondaryContainerLight,
    onSecondaryContainer = PaintOnSecondaryContainerLight,
    tertiary = PaintTertiaryLight,
    onTertiary = PaintOnTertiaryLight,
    tertiaryContainer = PaintTertiaryContainerLight,
    onTertiaryContainer = PaintOnTertiaryContainerLight,
    background = PaintBackgroundLight,
    onBackground = PaintOnBackgroundLight,
    surface = PaintSurfaceLight,
    onSurface = PaintOnSurfaceLight,
    surfaceVariant = PaintSurfaceVariantLight,
    onSurfaceVariant = PaintOnSurfaceVariantLight,
    outline = PaintOutlineLight
)

@Composable
fun PaintCoverTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

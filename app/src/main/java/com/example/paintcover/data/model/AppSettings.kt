package com.example.paintcover.data.model

enum class AppTheme(val displayName: String) {
    SYSTEM("System"),
    LIGHT("Light"),
    DARK("Dark")
}

data class AppSettings(
    val theme: AppTheme = AppTheme.SYSTEM,
    val dynamicColor: Boolean = true,
    val hapticFeedback: Boolean = true,
    val animationsEnabled: Boolean = true,
    val decimalPlaces: Int = 4,
    val roundingMode: DisplayRoundingMode = DisplayRoundingMode.HALF_UP,
    val showFormula: Boolean = true,
    val showCalculationSteps: Boolean = true,
    val showPaintDiagram: Boolean = true,
    val showPurchaseDetails: Boolean = true,
    val confirmDeleteHistory: Boolean = true
)

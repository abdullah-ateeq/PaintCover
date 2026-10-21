package com.example.paintcover.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.paintcover.data.model.*

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("paint_cover_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_THEME = "key_theme"
        private const val KEY_DYNAMIC_COLOR = "key_dynamic_color"
        private const val KEY_HAPTICS = "key_haptics"
        private const val KEY_ANIMATIONS = "key_animations"
        private const val KEY_DECIMAL_PLACES = "key_decimal_places"
        private const val KEY_ROUNDING_MODE = "key_rounding_mode"
        private const val KEY_SHOW_FORMULA = "key_show_formula"
        private const val KEY_SHOW_STEPS = "key_show_steps"
        private const val KEY_SHOW_DIAGRAM = "key_show_diagram"
        private const val KEY_SHOW_PURCHASE = "key_show_purchase"
        private const val KEY_CONFIRM_DELETE = "key_confirm_delete"

        // Last calculation inputs
        private const val KEY_LAST_SURFACE_TYPE = "key_last_surface_type"
        private const val KEY_LAST_INPUT_MODE = "key_last_input_mode"
        private const val KEY_LAST_SURFACE_AREA = "key_last_surface_area"
        private const val KEY_LAST_SURFACE_LENGTH = "key_last_surface_length"
        private const val KEY_LAST_SURFACE_WIDTH = "key_last_surface_width"
        private const val KEY_LAST_AREA_UNIT = "key_last_area_unit"
        private const val KEY_LAST_LENGTH_UNIT = "key_last_length_unit"
        private const val KEY_LAST_WIDTH_UNIT = "key_last_width_unit"
        private const val KEY_LAST_COVERAGE = "key_last_coverage"
        private const val KEY_LAST_COVERAGE_UNIT = "key_last_coverage_unit"
        private const val KEY_LAST_COATS = "key_last_coats"
        private const val KEY_LAST_WASTE = "key_last_waste"
        private const val KEY_LAST_CAN_SIZE = "key_last_can_size"
        private const val KEY_LAST_CAN_UNIT = "key_last_can_unit"
    }

    fun getSettings(): AppSettings {
        val themeName = prefs.getString(KEY_THEME, AppTheme.SYSTEM.name) ?: AppTheme.SYSTEM.name
        val theme = try { AppTheme.valueOf(themeName) } catch (e: Exception) { AppTheme.SYSTEM }

        val roundingModeName = prefs.getString(KEY_ROUNDING_MODE, DisplayRoundingMode.HALF_UP.name)
            ?: DisplayRoundingMode.HALF_UP.name
        val roundingMode = try { DisplayRoundingMode.valueOf(roundingModeName) } catch (e: Exception) { DisplayRoundingMode.HALF_UP }

        return AppSettings(
            theme = theme,
            dynamicColor = prefs.getBoolean(KEY_DYNAMIC_COLOR, true),
            hapticFeedback = prefs.getBoolean(KEY_HAPTICS, true),
            animationsEnabled = prefs.getBoolean(KEY_ANIMATIONS, true),
            decimalPlaces = prefs.getInt(KEY_DECIMAL_PLACES, 4),
            roundingMode = roundingMode,
            showFormula = prefs.getBoolean(KEY_SHOW_FORMULA, true),
            showCalculationSteps = prefs.getBoolean(KEY_SHOW_STEPS, true),
            showPaintDiagram = prefs.getBoolean(KEY_SHOW_DIAGRAM, true),
            showPurchaseDetails = prefs.getBoolean(KEY_SHOW_PURCHASE, true),
            confirmDeleteHistory = prefs.getBoolean(KEY_CONFIRM_DELETE, true)
        )
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit()
            .putString(KEY_THEME, settings.theme.name)
            .putBoolean(KEY_DYNAMIC_COLOR, settings.dynamicColor)
            .putBoolean(KEY_HAPTICS, settings.hapticFeedback)
            .putBoolean(KEY_ANIMATIONS, settings.animationsEnabled)
            .putInt(KEY_DECIMAL_PLACES, settings.decimalPlaces)
            .putString(KEY_ROUNDING_MODE, settings.roundingMode.name)
            .putBoolean(KEY_SHOW_FORMULA, settings.showFormula)
            .putBoolean(KEY_SHOW_STEPS, settings.showCalculationSteps)
            .putBoolean(KEY_SHOW_DIAGRAM, settings.showPaintDiagram)
            .putBoolean(KEY_SHOW_PURCHASE, settings.showPurchaseDetails)
            .putBoolean(KEY_CONFIRM_DELETE, settings.confirmDeleteHistory)
            .apply()
    }

    fun resetSettings(): AppSettings {
        val default = AppSettings()
        saveSettings(default)
        return default
    }

    fun saveLastInputs(
        surfaceType: SurfaceType,
        inputMode: InputMode,
        surfaceArea: String,
        length: String,
        width: String,
        areaUnit: AreaUnit,
        lengthUnit: LengthUnit,
        widthUnit: LengthUnit,
        coverage: String,
        coverageUnit: CoverageUnit,
        coats: String,
        waste: String,
        canSize: String,
        canUnit: PaintVolumeUnit
    ) {
        prefs.edit()
            .putString(KEY_LAST_SURFACE_TYPE, surfaceType.name)
            .putString(KEY_LAST_INPUT_MODE, inputMode.name)
            .putString(KEY_LAST_SURFACE_AREA, surfaceArea)
            .putString(KEY_LAST_SURFACE_LENGTH, length)
            .putString(KEY_LAST_SURFACE_WIDTH, width)
            .putString(KEY_LAST_AREA_UNIT, areaUnit.name)
            .putString(KEY_LAST_LENGTH_UNIT, lengthUnit.name)
            .putString(KEY_LAST_WIDTH_UNIT, widthUnit.name)
            .putString(KEY_LAST_COVERAGE, coverage)
            .putString(KEY_LAST_COVERAGE_UNIT, coverageUnit.name)
            .putString(KEY_LAST_COATS, coats)
            .putString(KEY_LAST_WASTE, waste)
            .putString(KEY_LAST_CAN_SIZE, canSize)
            .putString(KEY_LAST_CAN_UNIT, canUnit.name)
            .apply()
    }

    fun getLastInputs(): Map<String, Any?> {
        return mapOf(
            KEY_LAST_SURFACE_TYPE to prefs.getString(KEY_LAST_SURFACE_TYPE, null),
            KEY_LAST_INPUT_MODE to prefs.getString(KEY_LAST_INPUT_MODE, null),
            KEY_LAST_SURFACE_AREA to prefs.getString(KEY_LAST_SURFACE_AREA, null),
            KEY_LAST_SURFACE_LENGTH to prefs.getString(KEY_LAST_SURFACE_LENGTH, null),
            KEY_LAST_SURFACE_WIDTH to prefs.getString(KEY_LAST_SURFACE_WIDTH, null),
            KEY_LAST_AREA_UNIT to prefs.getString(KEY_LAST_AREA_UNIT, null),
            KEY_LAST_LENGTH_UNIT to prefs.getString(KEY_LAST_LENGTH_UNIT, null),
            KEY_LAST_WIDTH_UNIT to prefs.getString(KEY_LAST_WIDTH_UNIT, null),
            KEY_LAST_COVERAGE to prefs.getString(KEY_LAST_COVERAGE, null),
            KEY_LAST_COVERAGE_UNIT to prefs.getString(KEY_LAST_COVERAGE_UNIT, null),
            KEY_LAST_COATS to prefs.getString(KEY_LAST_COATS, null),
            KEY_LAST_WASTE to prefs.getString(KEY_LAST_WASTE, null),
            KEY_LAST_CAN_SIZE to prefs.getString(KEY_LAST_CAN_SIZE, null),
            KEY_LAST_CAN_UNIT to prefs.getString(KEY_LAST_CAN_UNIT, null)
        )
    }
}

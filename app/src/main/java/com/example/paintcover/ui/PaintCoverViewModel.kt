package com.example.paintcover.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paintcover.data.model.*
import com.example.paintcover.data.repository.HistoryRepository
import com.example.paintcover.data.repository.SettingsRepository
import com.example.paintcover.domain.math.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import java.util.UUID

class PaintCoverViewModel(
    private val settingsRepository: SettingsRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaintCoverUiState())
    val uiState: StateFlow<PaintCoverUiState> = _uiState.asStateFlow()

    init {
        loadSettingsAndHistory()
        calculate()
    }

    private fun loadSettingsAndHistory() {
        val settings = settingsRepository.getSettings()
        val history = historyRepository.getHistory()
        val lastInputs = settingsRepository.getLastInputs()

        val surfaceTypeStr = lastInputs["key_last_surface_type"] as? String
        val surfaceType = surfaceTypeStr?.let {
            try { SurfaceType.valueOf(it) } catch (e: Exception) { SurfaceType.WALL }
        } ?: SurfaceType.WALL

        val inputModeStr = lastInputs["key_last_input_mode"] as? String
        val inputMode = inputModeStr?.let {
            try { InputMode.valueOf(it) } catch (e: Exception) { InputMode.AREA }
        } ?: InputMode.AREA

        val surfaceArea = (lastInputs["key_last_surface_area"] as? String) ?: "50"
        val surfaceLength = (lastInputs["key_last_surface_length"] as? String) ?: "5"
        val surfaceWidth = (lastInputs["key_last_surface_width"] as? String) ?: "10"

        val areaUnitStr = lastInputs["key_last_area_unit"] as? String
        val areaUnit = areaUnitStr?.let {
            try { AreaUnit.valueOf(it) } catch (e: Exception) { AreaUnit.SQUARE_METER }
        } ?: AreaUnit.SQUARE_METER

        val lengthUnitStr = lastInputs["key_last_length_unit"] as? String
        val lengthUnit = lengthUnitStr?.let {
            try { LengthUnit.valueOf(it) } catch (e: Exception) { LengthUnit.METER }
        } ?: LengthUnit.METER

        val widthUnitStr = lastInputs["key_last_width_unit"] as? String
        val widthUnit = widthUnitStr?.let {
            try { LengthUnit.valueOf(it) } catch (e: Exception) { LengthUnit.METER }
        } ?: LengthUnit.METER

        val coverage = (lastInputs["key_last_coverage"] as? String) ?: "10"
        val coverageUnitStr = lastInputs["key_last_coverage_unit"] as? String
        val coverageUnit = coverageUnitStr?.let {
            try { CoverageUnit.valueOf(it) } catch (e: Exception) { CoverageUnit.SQUARE_METER_PER_LITER }
        } ?: CoverageUnit.SQUARE_METER_PER_LITER

        val coats = (lastInputs["key_last_coats"] as? String) ?: "2"
        val waste = (lastInputs["key_last_waste"] as? String) ?: "10"
        val canSize = (lastInputs["key_last_can_size"] as? String) ?: ""
        val canUnitStr = lastInputs["key_last_can_unit"] as? String
        val canUnit = canUnitStr?.let {
            try { PaintVolumeUnit.valueOf(it) } catch (e: Exception) { PaintVolumeUnit.LITER }
        } ?: PaintVolumeUnit.LITER

        _uiState.update {
            it.copy(
                settings = settings,
                history = history,
                surfaceType = surfaceType,
                inputMode = inputMode,
                surfaceAreaInput = surfaceArea,
                surfaceLengthInput = surfaceLength,
                surfaceWidthInput = surfaceWidth,
                surfaceAreaUnit = areaUnit,
                surfaceLengthUnit = lengthUnit,
                surfaceWidthUnit = widthUnit,
                coverageInput = coverage,
                coverageUnit = coverageUnit,
                coatsInput = coats,
                wastePercentInput = waste,
                canSizeInput = canSize,
                canSizeUnit = canUnit
            )
        }
    }

    fun onSurfaceTypeSelected(surfaceType: SurfaceType) {
        _uiState.update { it.copy(surfaceType = surfaceType) }
        calculate()
    }

    fun onInputModeSelected(inputMode: InputMode) {
        _uiState.update { it.copy(inputMode = inputMode) }
        calculate()
    }

    fun onSurfaceAreaChanged(input: String) {
        _uiState.update { it.copy(surfaceAreaInput = input) }
        calculate()
    }

    fun onSurfaceLengthChanged(input: String) {
        _uiState.update { it.copy(surfaceLengthInput = input) }
        calculate()
    }

    fun onSurfaceWidthChanged(input: String) {
        _uiState.update { it.copy(surfaceWidthInput = input) }
        calculate()
    }

    fun onSurfaceAreaUnitChanged(unit: AreaUnit) {
        _uiState.update { it.copy(surfaceAreaUnit = unit) }
        calculate()
    }

    fun onSurfaceLengthUnitChanged(unit: LengthUnit) {
        _uiState.update { it.copy(surfaceLengthUnit = unit) }
        calculate()
    }

    fun onSurfaceWidthUnitChanged(unit: LengthUnit) {
        _uiState.update { it.copy(surfaceWidthUnit = unit) }
        calculate()
    }

    fun onCoverageChanged(input: String) {
        _uiState.update { it.copy(coverageInput = input) }
        calculate()
    }

    fun onCoverageUnitChanged(unit: CoverageUnit) {
        _uiState.update { it.copy(coverageUnit = unit) }
        calculate()
    }

    fun onCoatsChanged(input: String) {
        _uiState.update { it.copy(coatsInput = input) }
        calculate()
    }

    fun onWastePercentChanged(input: String) {
        _uiState.update { it.copy(wastePercentInput = input) }
        calculate()
    }

    fun onCanSizeChanged(input: String) {
        _uiState.update { it.copy(canSizeInput = input) }
        calculate()
    }

    fun onCanSizeUnitChanged(unit: PaintVolumeUnit) {
        _uiState.update { it.copy(canSizeUnit = unit) }
        calculate()
    }

    fun onOpeningsEnabledChanged(enabled: Boolean) {
        _uiState.update { it.copy(openingsEnabled = enabled) }
        calculate()
    }

    fun addDoor(width: String = "0.9", height: String = "2.1", unit: LengthUnit = LengthUnit.METER) {
        val newDoor = Opening(
            id = UUID.randomUUID().toString(),
            width = width,
            height = height,
            widthUnit = unit,
            heightUnit = unit,
            label = "Door"
        )
        _uiState.update { it.copy(doors = it.doors + newDoor) }
        calculate()
    }

    fun updateDoor(id: String, width: String, height: String, widthUnit: LengthUnit, heightUnit: LengthUnit) {
        _uiState.update { state ->
            val updated = state.doors.map {
                if (it.id == id) it.copy(width = width, height = height, widthUnit = widthUnit, heightUnit = heightUnit) else it
            }
            state.copy(doors = updated)
        }
        calculate()
    }

    fun removeDoor(id: String) {
        _uiState.update { state ->
            state.copy(doors = state.doors.filter { it.id != id })
        }
        calculate()
    }

    fun addWindow(width: String = "1.2", height: String = "1.0", unit: LengthUnit = LengthUnit.METER) {
        val newWindow = Opening(
            id = UUID.randomUUID().toString(),
            width = width,
            height = height,
            widthUnit = unit,
            heightUnit = unit,
            label = "Window"
        )
        _uiState.update { it.copy(windows = it.windows + newWindow) }
        calculate()
    }

    fun updateWindow(id: String, width: String, height: String, widthUnit: LengthUnit, heightUnit: LengthUnit) {
        _uiState.update { state ->
            val updated = state.windows.map {
                if (it.id == id) it.copy(width = width, height = height, widthUnit = widthUnit, heightUnit = heightUnit) else it
            }
            state.copy(windows = updated)
        }
        calculate()
    }

    fun removeWindow(id: String) {
        _uiState.update { state ->
            state.copy(windows = state.windows.filter { it.id != id })
        }
        calculate()
    }

    fun calculate() {
        val state = _uiState.value
        val mathContext = PaintCoverCalculator.DEFAULT_MATH_CONTEXT

        // 1. Surface Gross Area
        val grossAreaM2: BigDecimal
        when (state.inputMode) {
            InputMode.AREA -> {
                val areaResult = PaintCoverParser.parseNumber(state.surfaceAreaInput)
                if (areaResult.isFailure) {
                    _uiState.update { it.copy(result = null, errorMessage = areaResult.exceptionOrNull()?.message ?: "Enter a valid number.") }
                    return
                }
                val area = areaResult.getOrThrow()
                val areaErr = PaintCoverValidator.validateArea(area)
                if (areaErr != null) {
                    _uiState.update { it.copy(result = null, errorMessage = areaErr.message) }
                    return
                }
                grossAreaM2 = AreaUnitConverter.toSquareMeters(area, state.surfaceAreaUnit, mathContext)
            }
            InputMode.DIMENSIONS -> {
                val lenResult = PaintCoverParser.parseNumber(state.surfaceLengthInput)
                if (lenResult.isFailure) {
                    _uiState.update { it.copy(result = null, errorMessage = lenResult.exceptionOrNull()?.message ?: "Enter a valid number.") }
                    return
                }
                val widthResult = PaintCoverParser.parseNumber(state.surfaceWidthInput)
                if (widthResult.isFailure) {
                    _uiState.update { it.copy(result = null, errorMessage = widthResult.exceptionOrNull()?.message ?: "Enter a valid number.") }
                    return
                }
                val length = lenResult.getOrThrow()
                val width = widthResult.getOrThrow()
                val lenErr = PaintCoverValidator.validateDimension(length)
                if (lenErr != null) {
                    _uiState.update { it.copy(result = null, errorMessage = lenErr.message) }
                    return
                }
                val widthErr = PaintCoverValidator.validateDimension(width)
                if (widthErr != null) {
                    _uiState.update { it.copy(result = null, errorMessage = widthErr.message) }
                    return
                }
                val lengthM = LengthUnitConverter.toMeters(length, state.surfaceLengthUnit, mathContext)
                val widthM = LengthUnitConverter.toMeters(width, state.surfaceWidthUnit, mathContext)
                grossAreaM2 = PaintCoverCalculator.calculateSurfaceArea(lengthM, widthM, mathContext)
            }
        }

        // 2. Openings
        val openingAreaM2: BigDecimal
        if (state.openingsEnabled && (state.doors.isNotEmpty() || state.windows.isNotEmpty())) {
            openingAreaM2 = PaintCoverCalculator.calculateTotalOpeningArea(state.doors + state.windows, mathContext)
            val openingErr = PaintCoverValidator.validatePaintableArea(grossAreaM2, openingAreaM2)
            if (openingErr != null) {
                _uiState.update { it.copy(result = null, errorMessage = openingErr.message) }
                return
            }
        } else {
            openingAreaM2 = BigDecimal.ZERO
        }

        val paintableAreaM2 = PaintCoverCalculator.calculatePaintableArea(grossAreaM2, openingAreaM2, mathContext)
        if (paintableAreaM2 <= BigDecimal.ZERO) {
            _uiState.update { it.copy(result = null, errorMessage = "Opening area must be smaller than the surface area.") }
            return
        }

        // 3. Paint Coverage
        val covResult = PaintCoverParser.parseNumber(state.coverageInput)
        if (covResult.isFailure) {
            _uiState.update { it.copy(result = null, errorMessage = covResult.exceptionOrNull()?.message ?: "Enter a valid number.") }
            return
        }
        val coverageVal = covResult.getOrThrow()
        val covErr = PaintCoverValidator.validateCoverage(coverageVal)
        if (covErr != null) {
            _uiState.update { it.copy(result = null, errorMessage = covErr.message) }
            return
        }
        val coveragePerLiter = CoverageConverter.toSquareMetersPerLiter(coverageVal, state.coverageUnit, mathContext)

        // 4. Coats
        val (coatsVal, coatsErr) = PaintCoverValidator.validateCoats(state.coatsInput)
        if (coatsErr != null || coatsVal == null) {
            _uiState.update { it.copy(result = null, errorMessage = coatsErr?.message ?: "Number of coats must be a positive whole number.") }
            return
        }

        // 5. Waste
        val wasteResult = PaintCoverParser.parseNumber(state.wastePercentInput)
        if (wasteResult.isFailure) {
            _uiState.update { it.copy(result = null, errorMessage = wasteResult.exceptionOrNull()?.message ?: "Enter a valid number.") }
            return
        }
        val wasteVal = wasteResult.getOrThrow()
        val wasteErr = PaintCoverValidator.validateWastePercent(wasteVal)
        if (wasteErr != null) {
            _uiState.update { it.copy(result = null, errorMessage = wasteErr.message) }
            return
        }

        // 6. Base Paint & Paint With Waste
        val basePaint = PaintCoverCalculator.calculatePaintRequired(
            paintableArea = paintableAreaM2,
            coats = BigInteger.valueOf(coatsVal.toLong()),
            coveragePerLiter = coveragePerLiter,
            mathContext = mathContext
        )

        val paintWithWaste = PaintCoverCalculator.calculatePaintWithWaste(
            paintRequired = basePaint,
            wastePercent = wasteVal,
            mathContext = mathContext
        )

        // 7. Can Size (Optional)
        var canSizeInLiters: BigDecimal? = null
        var cansRequired: BigInteger? = null
        var totalPurchased: BigDecimal? = null
        var leftover: BigDecimal? = null

        val trimmedCanInput = state.canSizeInput.trim()
        if (trimmedCanInput.isNotEmpty()) {
            val canResult = PaintCoverParser.parseNumber(trimmedCanInput)
            if (canResult.isFailure) {
                _uiState.update { it.copy(result = null, errorMessage = canResult.exceptionOrNull()?.message ?: "Enter a valid number.") }
                return
            }
            val canVal = canResult.getOrThrow()
            val canErr = PaintCoverValidator.validateCanSize(canVal)
            if (canErr != null) {
                _uiState.update { it.copy(result = null, errorMessage = canErr.message) }
                return
            }
            canSizeInLiters = PaintVolumeConverter.toLiters(canVal, state.canSizeUnit, mathContext)
            cansRequired = PaintCoverCalculator.calculateCansRequired(paintWithWaste, canSizeInLiters)
            totalPurchased = PaintCoverCalculator.calculateTotalPurchased(cansRequired, canSizeInLiters, mathContext)
            leftover = PaintCoverCalculator.calculateLeftover(totalPurchased, paintWithWaste, mathContext)
        }

        // 8. Calculation Steps
        val steps = PaintCoverCalculator.generateCalculationSteps(
            grossArea = grossAreaM2,
            openingArea = openingAreaM2,
            paintableArea = paintableAreaM2,
            coveragePerLiter = coveragePerLiter,
            coats = coatsVal,
            basePaint = basePaint,
            wastePercent = wasteVal,
            paintWithWaste = paintWithWaste,
            canSize = canSizeInLiters,
            cansRequired = cansRequired,
            totalPurchased = totalPurchased,
            leftover = leftover
        )

        val result = PaintCoverResult(
            surfaceType = state.surfaceType,
            grossSurfaceArea = grossAreaM2,
            openingArea = openingAreaM2,
            paintableArea = paintableAreaM2,
            coveragePerLiter = coveragePerLiter,
            coats = coatsVal,
            basePaintRequired = basePaint,
            wastePercent = wasteVal,
            paintRequired = paintWithWaste,
            canSize = canSizeInLiters,
            cansRequired = cansRequired,
            totalPurchasedPaint = totalPurchased,
            leftoverPaint = leftover,
            calculationSteps = steps,
            calculatedAt = System.currentTimeMillis(),
            surfaceAreaUnit = state.surfaceAreaUnit,
            coverageUnit = state.coverageUnit,
            paintVolumeUnit = state.canSizeUnit
        )

        _uiState.update {
            it.copy(
                result = result,
                errorMessage = null
            )
        }

        // Persist last inputs
        settingsRepository.saveLastInputs(
            surfaceType = state.surfaceType,
            inputMode = state.inputMode,
            surfaceArea = state.surfaceAreaInput,
            length = state.surfaceLengthInput,
            width = state.surfaceWidthInput,
            areaUnit = state.surfaceAreaUnit,
            lengthUnit = state.surfaceLengthUnit,
            widthUnit = state.surfaceWidthUnit,
            coverage = state.coverageInput,
            coverageUnit = state.coverageUnit,
            coats = state.coatsInput,
            waste = state.wastePercentInput,
            canSize = state.canSizeInput,
            canUnit = state.canSizeUnit
        )
    }

    fun saveCurrentToHistory() {
        val state = _uiState.value
        val result = state.result ?: return

        val decPlaces = state.settings.decimalPlaces
        val roundMode = state.settings.roundingMode

        val newEntry = PaintCoverHistory(
            id = UUID.randomUUID().toString(),
            surfaceType = state.surfaceType,
            inputMode = state.inputMode,
            surfaceArea = if (state.inputMode == InputMode.AREA) state.surfaceAreaInput else null,
            surfaceLength = if (state.inputMode == InputMode.DIMENSIONS) state.surfaceLengthInput else null,
            surfaceWidth = if (state.inputMode == InputMode.DIMENSIONS) state.surfaceWidthInput else null,
            surfaceAreaUnit = if (state.inputMode == InputMode.AREA) state.surfaceAreaUnit else null,
            surfaceLengthUnit = if (state.inputMode == InputMode.DIMENSIONS) state.surfaceLengthUnit else null,
            surfaceWidthUnit = if (state.inputMode == InputMode.DIMENSIONS) state.surfaceWidthUnit else null,
            coverage = state.coverageInput,
            coverageUnit = state.coverageUnit,
            coats = result.coats,
            wastePercent = state.wastePercentInput,
            canSize = state.canSizeInput.takeIf { it.isNotBlank() },
            canSizeUnit = state.canSizeUnit.takeIf { state.canSizeInput.isNotBlank() },
            savedAt = System.currentTimeMillis(),
            calculatedPaintRequiredDisplay = "${PaintCoverFormatter.formatBigDecimal(result.paintRequired, decPlaces, roundMode)} L",
            paintableAreaDisplay = "${PaintCoverFormatter.formatBigDecimal(result.paintableArea, decPlaces, roundMode)} m²"
        )

        val updatedHistory = historyRepository.addEntry(newEntry)
        _uiState.update { it.copy(history = updatedHistory, userNotice = "Calculation saved to history") }
    }

    fun restoreHistory(entry: PaintCoverHistory) {
        _uiState.update {
            it.copy(
                surfaceType = entry.surfaceType,
                inputMode = entry.inputMode,
                surfaceAreaInput = entry.surfaceArea ?: it.surfaceAreaInput,
                surfaceLengthInput = entry.surfaceLength ?: it.surfaceLengthInput,
                surfaceWidthInput = entry.surfaceWidth ?: it.surfaceWidthInput,
                surfaceAreaUnit = entry.surfaceAreaUnit ?: it.surfaceAreaUnit,
                surfaceLengthUnit = entry.surfaceLengthUnit ?: it.surfaceLengthUnit,
                surfaceWidthUnit = entry.surfaceWidthUnit ?: it.surfaceWidthUnit,
                coverageInput = entry.coverage,
                coverageUnit = entry.coverageUnit,
                coatsInput = entry.coats.toString(),
                wastePercentInput = entry.wastePercent,
                canSizeInput = entry.canSize ?: "",
                canSizeUnit = entry.canSizeUnit ?: PaintVolumeUnit.LITER,
                userNotice = "Restored calculation from history"
            )
        }
        calculate()
    }

    fun deleteHistory(id: String) {
        val updated = historyRepository.deleteEntry(id)
        _uiState.update { it.copy(history = updated) }
    }

    fun clearAllHistory() {
        val updated = historyRepository.clearAll()
        _uiState.update { it.copy(history = updated, userNotice = "History cleared") }
    }

    fun reset() {
        _uiState.update {
            it.copy(
                surfaceType = SurfaceType.WALL,
                inputMode = InputMode.AREA,
                surfaceAreaInput = "50",
                surfaceLengthInput = "5",
                surfaceWidthInput = "10",
                surfaceAreaUnit = AreaUnit.SQUARE_METER,
                surfaceLengthUnit = LengthUnit.METER,
                surfaceWidthUnit = LengthUnit.METER,
                coverageInput = "10",
                coverageUnit = CoverageUnit.SQUARE_METER_PER_LITER,
                coatsInput = "2",
                wastePercentInput = "10",
                canSizeInput = "",
                canSizeUnit = PaintVolumeUnit.LITER,
                openingsEnabled = false,
                doors = emptyList(),
                windows = emptyList(),
                errorMessage = null,
                userNotice = "Reset to defaults"
            )
        }
        calculate()
    }

    fun updateSettings(settings: AppSettings) {
        settingsRepository.saveSettings(settings)
        _uiState.update { it.copy(settings = settings) }
    }

    fun resetSettings() {
        val defaultSettings = settingsRepository.resetSettings()
        _uiState.update { it.copy(settings = defaultSettings, userNotice = "Settings reset to default") }
    }

    fun clearUserNotice() {
        _uiState.update { it.copy(userNotice = null) }
    }
}

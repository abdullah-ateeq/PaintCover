package com.example.paintcover.data.model

data class PaintCoverUiState(
    val surfaceType: SurfaceType = SurfaceType.WALL,
    val inputMode: InputMode = InputMode.AREA,
    val surfaceAreaInput: String = "50",
    val surfaceLengthInput: String = "5",
    val surfaceWidthInput: String = "10",
    val surfaceAreaUnit: AreaUnit = AreaUnit.SQUARE_METER,
    val surfaceLengthUnit: LengthUnit = LengthUnit.METER,
    val surfaceWidthUnit: LengthUnit = LengthUnit.METER,
    val coverageInput: String = "10",
    val coverageUnit: CoverageUnit = CoverageUnit.SQUARE_METER_PER_LITER,
    val coatsInput: String = "2",
    val wastePercentInput: String = "10",
    val canSizeInput: String = "",
    val canSizeUnit: PaintVolumeUnit = PaintVolumeUnit.LITER,
    val openingsEnabled: Boolean = false,
    val doors: List<Opening> = emptyList(),
    val windows: List<Opening> = emptyList(),
    val result: PaintCoverResult? = null,
    val history: List<PaintCoverHistory> = emptyList(),
    val errorMessage: String? = null,
    val isCalculating: Boolean = false,
    val settings: AppSettings = AppSettings(),
    val showCalculationDetails: Boolean = true,
    val userNotice: String? = null
)

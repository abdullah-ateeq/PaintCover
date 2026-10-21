package com.example.paintcover.data.model

data class PaintCoverHistory(
    val id: String,
    val surfaceType: SurfaceType,
    val inputMode: InputMode,
    val surfaceArea: String?,
    val surfaceLength: String?,
    val surfaceWidth: String?,
    val surfaceAreaUnit: AreaUnit?,
    val surfaceLengthUnit: LengthUnit?,
    val surfaceWidthUnit: LengthUnit?,
    val coverage: String,
    val coverageUnit: CoverageUnit,
    val coats: Int,
    val wastePercent: String,
    val canSize: String?,
    val canSizeUnit: PaintVolumeUnit?,
    val savedAt: Long,
    val calculatedPaintRequiredDisplay: String = "",
    val paintableAreaDisplay: String = ""
)

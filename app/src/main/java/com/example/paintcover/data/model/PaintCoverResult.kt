package com.example.paintcover.data.model

import java.math.BigDecimal
import java.math.BigInteger

data class PaintCoverResult(
    val surfaceType: SurfaceType,
    val grossSurfaceArea: BigDecimal,
    val openingArea: BigDecimal,
    val paintableArea: BigDecimal,
    val coveragePerLiter: BigDecimal,
    val coats: Int,
    val basePaintRequired: BigDecimal,
    val wastePercent: BigDecimal,
    val paintRequired: BigDecimal,
    val canSize: BigDecimal?,
    val cansRequired: BigInteger?,
    val totalPurchasedPaint: BigDecimal?,
    val leftoverPaint: BigDecimal?,
    val calculationSteps: List<CalculationStep>,
    val calculatedAt: Long,
    val surfaceAreaUnit: AreaUnit = AreaUnit.SQUARE_METER,
    val coverageUnit: CoverageUnit = CoverageUnit.SQUARE_METER_PER_LITER,
    val paintVolumeUnit: PaintVolumeUnit = PaintVolumeUnit.LITER
)

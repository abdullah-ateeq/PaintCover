package com.example.paintcover.domain.math

import com.example.paintcover.data.model.CoverageUnit
import java.math.BigDecimal
import java.math.MathContext

object CoverageConverter {
    private val SQ_FOOT_IN_SQ_METERS = BigDecimal("0.09290304")
    private val US_GALLON_IN_LITERS = BigDecimal("3.785411784")

    fun toSquareMetersPerLiter(
        coverage: BigDecimal,
        unit: CoverageUnit,
        mathContext: MathContext
    ): BigDecimal {
        return when (unit) {
            CoverageUnit.SQUARE_METER_PER_LITER -> coverage
            CoverageUnit.SQUARE_FOOT_PER_LITER -> coverage.multiply(SQ_FOOT_IN_SQ_METERS, mathContext)
            CoverageUnit.SQUARE_METER_PER_GALLON -> coverage.divide(US_GALLON_IN_LITERS, mathContext)
            CoverageUnit.SQUARE_FOOT_PER_GALLON -> coverage
                .multiply(SQ_FOOT_IN_SQ_METERS, mathContext)
                .divide(US_GALLON_IN_LITERS, mathContext)
        }
    }

    fun fromSquareMetersPerLiter(
        coverageInM2PerLiter: BigDecimal,
        targetUnit: CoverageUnit,
        mathContext: MathContext
    ): BigDecimal {
        return when (targetUnit) {
            CoverageUnit.SQUARE_METER_PER_LITER -> coverageInM2PerLiter
            CoverageUnit.SQUARE_FOOT_PER_LITER -> coverageInM2PerLiter.divide(SQ_FOOT_IN_SQ_METERS, mathContext)
            CoverageUnit.SQUARE_METER_PER_GALLON -> coverageInM2PerLiter.multiply(US_GALLON_IN_LITERS, mathContext)
            CoverageUnit.SQUARE_FOOT_PER_GALLON -> coverageInM2PerLiter
                .multiply(US_GALLON_IN_LITERS, mathContext)
                .divide(SQ_FOOT_IN_SQ_METERS, mathContext)
        }
    }
}

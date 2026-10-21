package com.example.paintcover.domain.math

import com.example.paintcover.data.model.PaintVolumeUnit
import java.math.BigDecimal
import java.math.MathContext

object PaintVolumeConverter {
    // Exact factors relative to 1 Liter (L)
    val FACTOR_MILLILITER = BigDecimal("0.001")
    val FACTOR_LITER = BigDecimal.ONE
    val FACTOR_US_GALLON = BigDecimal("3.785411784")
    val FACTOR_US_FLUID_OUNCE = BigDecimal("0.0295735295625")

    fun factorToLiters(unit: PaintVolumeUnit): BigDecimal = when (unit) {
        PaintVolumeUnit.MILLILITER -> FACTOR_MILLILITER
        PaintVolumeUnit.LITER -> FACTOR_LITER
        PaintVolumeUnit.US_GALLON -> FACTOR_US_GALLON
        PaintVolumeUnit.US_FLUID_OUNCE -> FACTOR_US_FLUID_OUNCE
    }

    fun toLiters(value: BigDecimal, unit: PaintVolumeUnit, mathContext: MathContext): BigDecimal {
        return value.multiply(factorToLiters(unit), mathContext)
    }

    fun fromLiters(valueInLiters: BigDecimal, targetUnit: PaintVolumeUnit, mathContext: MathContext): BigDecimal {
        val factor = factorToLiters(targetUnit)
        return valueInLiters.divide(factor, mathContext)
    }
}

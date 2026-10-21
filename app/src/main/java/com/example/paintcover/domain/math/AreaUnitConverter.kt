package com.example.paintcover.domain.math

import com.example.paintcover.data.model.AreaUnit
import java.math.BigDecimal
import java.math.MathContext

object AreaUnitConverter {
    // Exact factors relative to 1 Square Meter (m²)
    private val FACTOR_SQ_MILLIMETER = BigDecimal("0.000001")
    private val FACTOR_SQ_CENTIMETER = BigDecimal("0.0001")
    private val FACTOR_SQ_METER = BigDecimal.ONE
    private val FACTOR_SQ_KILOMETER = BigDecimal("1000000")
    private val FACTOR_SQ_INCH = BigDecimal("0.00064516")
    private val FACTOR_SQ_FOOT = BigDecimal("0.09290304")
    private val FACTOR_SQ_YARD = BigDecimal("0.83612736")

    fun factorToSquareMeters(unit: AreaUnit): BigDecimal = when (unit) {
        AreaUnit.SQUARE_MILLIMETER -> FACTOR_SQ_MILLIMETER
        AreaUnit.SQUARE_CENTIMETER -> FACTOR_SQ_CENTIMETER
        AreaUnit.SQUARE_METER -> FACTOR_SQ_METER
        AreaUnit.SQUARE_KILOMETER -> FACTOR_SQ_KILOMETER
        AreaUnit.SQUARE_INCH -> FACTOR_SQ_INCH
        AreaUnit.SQUARE_FOOT -> FACTOR_SQ_FOOT
        AreaUnit.SQUARE_YARD -> FACTOR_SQ_YARD
    }

    fun toSquareMeters(value: BigDecimal, unit: AreaUnit, mathContext: MathContext): BigDecimal {
        return value.multiply(factorToSquareMeters(unit), mathContext)
    }

    fun fromSquareMeters(valueInSquareMeters: BigDecimal, targetUnit: AreaUnit, mathContext: MathContext): BigDecimal {
        val factor = factorToSquareMeters(targetUnit)
        return valueInSquareMeters.divide(factor, mathContext)
    }
}

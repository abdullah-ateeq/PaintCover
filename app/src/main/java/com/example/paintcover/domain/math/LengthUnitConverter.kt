package com.example.paintcover.domain.math

import com.example.paintcover.data.model.LengthUnit
import java.math.BigDecimal
import java.math.MathContext

object LengthUnitConverter {
    // Exact factors relative to 1 Meter
    private val FACTOR_MILLIMETER = BigDecimal("0.001")
    private val FACTOR_CENTIMETER = BigDecimal("0.01")
    private val FACTOR_METER = BigDecimal.ONE
    private val FACTOR_KILOMETER = BigDecimal("1000")
    private val FACTOR_INCH = BigDecimal("0.0254")
    private val FACTOR_FOOT = BigDecimal("0.3048")
    private val FACTOR_YARD = BigDecimal("0.9144")

    fun factorToMeters(unit: LengthUnit): BigDecimal = when (unit) {
        LengthUnit.MILLIMETER -> FACTOR_MILLIMETER
        LengthUnit.CENTIMETER -> FACTOR_CENTIMETER
        LengthUnit.METER -> FACTOR_METER
        LengthUnit.KILOMETER -> FACTOR_KILOMETER
        LengthUnit.INCH -> FACTOR_INCH
        LengthUnit.FOOT -> FACTOR_FOOT
        LengthUnit.YARD -> FACTOR_YARD
    }

    fun toMeters(value: BigDecimal, unit: LengthUnit, mathContext: MathContext): BigDecimal {
        return value.multiply(factorToMeters(unit), mathContext)
    }

    fun fromMeters(valueInMeters: BigDecimal, targetUnit: LengthUnit, mathContext: MathContext): BigDecimal {
        val factor = factorToMeters(targetUnit)
        return valueInMeters.divide(factor, mathContext)
    }
}

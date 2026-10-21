package com.example.paintcover.domain.math

import com.example.paintcover.data.model.CalculationStep
import com.example.paintcover.data.model.Opening
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

object PaintCoverCalculator {
    val DEFAULT_MATH_CONTEXT = MathContext(50, java.math.RoundingMode.HALF_UP)
    private val ONE_HUNDRED = BigDecimal("100")

    fun calculateSurfaceArea(
        length: BigDecimal,
        width: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        return length.multiply(width, mathContext)
    }

    fun calculateOpeningArea(
        width: BigDecimal,
        height: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        return width.multiply(height, mathContext)
    }

    fun calculateTotalOpeningArea(
        openings: List<Opening>,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        var total = BigDecimal.ZERO
        for (opening in openings) {
            val wResult = PaintCoverParser.parseNumber(opening.width)
            val hResult = PaintCoverParser.parseNumber(opening.height)
            if (wResult.isSuccess && hResult.isSuccess) {
                val w = wResult.getOrThrow()
                val h = hResult.getOrThrow()
                if (w > BigDecimal.ZERO && h > BigDecimal.ZERO) {
                    val wM = LengthUnitConverter.toMeters(w, opening.widthUnit, mathContext)
                    val hM = LengthUnitConverter.toMeters(h, opening.heightUnit, mathContext)
                    val area = calculateOpeningArea(wM, hM, mathContext)
                    total = total.add(area, mathContext)
                }
            }
        }
        return total
    }

    fun calculatePaintableArea(
        grossArea: BigDecimal,
        openingArea: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        return grossArea.subtract(openingArea, mathContext)
    }

    fun calculatePaintRequired(
        paintableArea: BigDecimal,
        coats: BigInteger,
        coveragePerLiter: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        val areaTimesCoats = paintableArea.multiply(BigDecimal(coats), mathContext)
        return areaTimesCoats.divide(coveragePerLiter, mathContext)
    }

    fun calculatePaintWithWaste(
        paintRequired: BigDecimal,
        wastePercent: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        val wasteFactor = BigDecimal.ONE.add(
            wastePercent.divide(ONE_HUNDRED, mathContext),
            mathContext
        )
        return paintRequired.multiply(wasteFactor, mathContext)
    }

    fun calculateCansRequired(
        paintRequired: BigDecimal,
        canSize: BigDecimal
    ): BigInteger {
        if (paintRequired <= BigDecimal.ZERO || canSize <= BigDecimal.ZERO) {
            return BigInteger.ZERO
        }
        val remainder = paintRequired.remainder(canSize)
        val quotient = paintRequired.divideToIntegralValue(canSize).toBigInteger()
        return if (remainder.compareTo(BigDecimal.ZERO) == 0) {
            quotient
        } else {
            quotient.add(BigInteger.ONE)
        }
    }

    fun calculateTotalPurchased(
        cansRequired: BigInteger,
        canSize: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        return BigDecimal(cansRequired).multiply(canSize, mathContext)
    }

    fun calculateLeftover(
        totalPurchased: BigDecimal,
        paintRequired: BigDecimal,
        mathContext: MathContext = DEFAULT_MATH_CONTEXT
    ): BigDecimal {
        val leftover = totalPurchased.subtract(paintRequired, mathContext)
        return if (leftover < BigDecimal.ZERO) BigDecimal.ZERO else leftover
    }

    fun generateCalculationSteps(
        grossArea: BigDecimal,
        openingArea: BigDecimal,
        paintableArea: BigDecimal,
        coveragePerLiter: BigDecimal,
        coats: Int,
        basePaint: BigDecimal,
        wastePercent: BigDecimal,
        paintWithWaste: BigDecimal,
        canSize: BigDecimal?,
        cansRequired: BigInteger?,
        totalPurchased: BigDecimal?,
        leftover: BigDecimal?
    ): List<CalculationStep> {
        val steps = mutableListOf<CalculationStep>()

        if (openingArea > BigDecimal.ZERO) {
            steps.add(
                CalculationStep(
                    label = "Gross Surface Area",
                    expression = "Total measured surface",
                    result = PaintCoverFormatter.formatBigDecimal(grossArea) + " m²"
                )
            )
            steps.add(
                CalculationStep(
                    label = "Deducted Openings",
                    expression = "Total area of doors and windows",
                    result = PaintCoverFormatter.formatBigDecimal(openingArea) + " m²"
                )
            )
            steps.add(
                CalculationStep(
                    label = "Paintable Area",
                    expression = "Gross Area - Openings (${PaintCoverFormatter.formatBigDecimal(grossArea)} - ${PaintCoverFormatter.formatBigDecimal(openingArea)})",
                    result = PaintCoverFormatter.formatBigDecimal(paintableArea) + " m²"
                )
            )
        } else {
            steps.add(
                CalculationStep(
                    label = "Paintable Area",
                    expression = "Surface Area",
                    result = PaintCoverFormatter.formatBigDecimal(paintableArea) + " m²"
                )
            )
        }

        steps.add(
            CalculationStep(
                label = "Base Paint",
                expression = "Area × Coats / Coverage (${PaintCoverFormatter.formatBigDecimal(paintableArea)} × $coats / ${PaintCoverFormatter.formatBigDecimal(coveragePerLiter)})",
                result = PaintCoverFormatter.formatBigDecimal(basePaint) + " L"
            )
        )

        steps.add(
            CalculationStep(
                label = "Paint With Waste",
                expression = "Base Paint × (1 + Waste% / 100) (${PaintCoverFormatter.formatBigDecimal(basePaint)} × (1 + ${PaintCoverFormatter.formatBigDecimal(wastePercent)} / 100))",
                result = PaintCoverFormatter.formatBigDecimal(paintWithWaste) + " L"
            )
        )

        if (canSize != null && cansRequired != null && totalPurchased != null && leftover != null) {
            val divResult = paintWithWaste.divide(canSize, DEFAULT_MATH_CONTEXT)
            steps.add(
                CalculationStep(
                    label = "Can Calculation",
                    expression = "Paint Required / Can Size (${PaintCoverFormatter.formatBigDecimal(paintWithWaste)} / ${PaintCoverFormatter.formatBigDecimal(canSize)} = ${PaintCoverFormatter.formatBigDecimal(divResult)})",
                    result = "CEILING = $cansRequired cans"
                )
            )
            steps.add(
                CalculationStep(
                    label = "Total Purchased",
                    expression = "Cans × Can Size ($cansRequired × ${PaintCoverFormatter.formatBigDecimal(canSize)})",
                    result = PaintCoverFormatter.formatBigDecimal(totalPurchased) + " L"
                )
            )
            steps.add(
                CalculationStep(
                    label = "Estimated Leftover",
                    expression = "Total Purchased - Paint Required (${PaintCoverFormatter.formatBigDecimal(totalPurchased)} - ${PaintCoverFormatter.formatBigDecimal(paintWithWaste)})",
                    result = PaintCoverFormatter.formatBigDecimal(leftover) + " L"
                )
            )
        }

        return steps
    }
}

package com.example.paintcover

import com.example.paintcover.data.model.DisplayRoundingMode
import com.example.paintcover.domain.math.PaintCoverCalculator
import com.example.paintcover.domain.math.PaintCoverFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

class FormatterAndStepsTest {

    @Test
    fun testTrailingZerosRemoval() {
        assertEquals("11", PaintCoverFormatter.formatBigDecimal(BigDecimal("11.0000"), 4))
        assertEquals("10.25", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.2500"), 4))
        assertEquals("10.1156", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.1156"), 4))
    }

    @Test
    fun testRoundingModes() {
        val testVal = BigDecimal("10.555")

        // 2 decimal places
        // HALF_UP: 10.555 -> 10.56
        assertEquals("10.56", PaintCoverFormatter.formatBigDecimal(testVal, 2, DisplayRoundingMode.HALF_UP))
        // HALF_DOWN: 10.555 -> 10.55
        assertEquals("10.55", PaintCoverFormatter.formatBigDecimal(testVal, 2, DisplayRoundingMode.HALF_DOWN))
        // UP: 10.551 -> 10.56
        assertEquals("10.56", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.551"), 2, DisplayRoundingMode.UP))
        // DOWN: 10.559 -> 10.55
        assertEquals("10.55", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.559"), 2, DisplayRoundingMode.DOWN))
        // CEILING: -10.559 -> -10.55, 10.551 -> 10.56
        assertEquals("10.56", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.551"), 2, DisplayRoundingMode.CEILING))
        // FLOOR: 10.559 -> 10.55
        assertEquals("10.55", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.559"), 2, DisplayRoundingMode.FLOOR))
        // HALF_EVEN: 10.555 (last digit before round is odd 5 -> 6) -> 10.56; 10.545 (even 4 -> 4) -> 10.54
        assertEquals("10.56", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.555"), 2, DisplayRoundingMode.HALF_EVEN))
        assertEquals("10.54", PaintCoverFormatter.formatBigDecimal(BigDecimal("10.545"), 2, DisplayRoundingMode.HALF_EVEN))
    }

    @Test
    fun testGenerateCalculationSteps() {
        val steps = PaintCoverCalculator.generateCalculationSteps(
            grossArea = BigDecimal("50"),
            openingArea = BigDecimal("4.02"),
            paintableArea = BigDecimal("45.98"),
            coveragePerLiter = BigDecimal("10"),
            coats = 2,
            basePaint = BigDecimal("9.196"),
            wastePercent = BigDecimal("10"),
            paintWithWaste = BigDecimal("10.1156"),
            canSize = BigDecimal("5"),
            cansRequired = BigInteger.valueOf(3),
            totalPurchased = BigDecimal("15"),
            leftover = BigDecimal("4.8844")
        )

        assertTrue(steps.any { it.label == "Gross Surface Area" })
        assertTrue(steps.any { it.label == "Deducted Openings" })
        assertTrue(steps.any { it.label == "Paintable Area" })
        assertTrue(steps.any { it.label == "Base Paint" })
        assertTrue(steps.any { it.label == "Paint With Waste" })
        assertTrue(steps.any { it.label == "Can Calculation" })
        assertTrue(steps.any { it.label == "Total Purchased" })
        assertTrue(steps.any { it.label == "Estimated Leftover" })
    }
}

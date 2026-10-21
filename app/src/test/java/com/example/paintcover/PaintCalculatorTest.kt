package com.example.paintcover

import com.example.paintcover.domain.math.PaintCoverCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext

class PaintCalculatorTest {

    private val mc = PaintCoverCalculator.DEFAULT_MATH_CONTEXT

    // Critical Test 1: Basic Paint
    // Area = 50 m², Coverage = 10 m²/L, Coats = 2, Waste = 10%
    // Base Paint = 10 L, Paint With Waste = 11 L
    @Test
    fun testBasicPaintCalculation() {
        val area = BigDecimal("50")
        val coverage = BigDecimal("10")
        val coats = BigInteger.valueOf(2)
        val wastePercent = BigDecimal("10")

        val basePaint = PaintCoverCalculator.calculatePaintRequired(area, coats, coverage, mc)
        assertEquals(0, BigDecimal("10").compareTo(basePaint))

        val paintWithWaste = PaintCoverCalculator.calculatePaintWithWaste(basePaint, wastePercent, mc)
        assertEquals(0, BigDecimal("11").compareTo(paintWithWaste))
    }

    // Critical Test 2: One Coat
    // Area = 50 m², Coverage = 10 m²/L, Coats = 1, Waste = 0% -> Paint Required = 5 L
    @Test
    fun testOneCoat() {
        val area = BigDecimal("50")
        val coverage = BigDecimal("10")
        val coats = BigInteger.valueOf(1)
        val waste = BigDecimal("0")

        val basePaint = PaintCoverCalculator.calculatePaintRequired(area, coats, coverage, mc)
        val finalPaint = PaintCoverCalculator.calculatePaintWithWaste(basePaint, waste, mc)
        assertEquals(0, BigDecimal("5").compareTo(finalPaint))
    }

    // Critical Test 3: Three Coats
    // Area = 50 m², Coverage = 10 m²/L, Coats = 3, Waste = 0% -> Paint Required = 15 L
    @Test
    fun testThreeCoats() {
        val area = BigDecimal("50")
        val coverage = BigDecimal("10")
        val coats = BigInteger.valueOf(3)
        val waste = BigDecimal("0")

        val basePaint = PaintCoverCalculator.calculatePaintRequired(area, coats, coverage, mc)
        val finalPaint = PaintCoverCalculator.calculatePaintWithWaste(basePaint, waste, mc)
        assertEquals(0, BigDecimal("15").compareTo(finalPaint))
    }

    // Critical Test 4: Can Calculation (Paint Required = 11 L, Can Size = 5 L)
    // Cans Required = 3, Total Purchased = 15 L, Leftover = 4 L
    @Test
    fun testCanCalculationFractional() {
        val paintRequired = BigDecimal("11")
        val canSize = BigDecimal("5")

        val cans = PaintCoverCalculator.calculateCansRequired(paintRequired, canSize)
        assertEquals(BigInteger.valueOf(3), cans)

        val totalPurchased = PaintCoverCalculator.calculateTotalPurchased(cans, canSize, mc)
        assertEquals(0, BigDecimal("15").compareTo(totalPurchased))

        val leftover = PaintCoverCalculator.calculateLeftover(totalPurchased, paintRequired, mc)
        assertEquals(0, BigDecimal("4").compareTo(leftover))
    }

    // Critical Test 5: Exact Can Division (Paint Required = 10 L, Can Size = 5 L)
    // Cans Required = 2, Total Purchased = 10 L, Leftover = 0 L
    @Test
    fun testCanCalculationExactDivision() {
        val paintRequired = BigDecimal("10")
        val canSize = BigDecimal("5")

        val cans = PaintCoverCalculator.calculateCansRequired(paintRequired, canSize)
        assertEquals(BigInteger.valueOf(2), cans)

        val totalPurchased = PaintCoverCalculator.calculateTotalPurchased(cans, canSize, mc)
        assertEquals(0, BigDecimal("10").compareTo(totalPurchased))

        val leftover = PaintCoverCalculator.calculateLeftover(totalPurchased, paintRequired, mc)
        assertEquals(0, BigDecimal.ZERO.compareTo(leftover))
    }

    // Critical Test 6: Dimension Mode
    // Length = 5 m, Width = 10 m -> Area = 50 m²
    @Test
    fun testDimensionModeSurfaceArea() {
        val length = BigDecimal("5")
        val width = BigDecimal("10")
        val area = PaintCoverCalculator.calculateSurfaceArea(length, width, mc)
        assertEquals(0, BigDecimal("50").compareTo(area))
    }

    // Critical Test 7: Openings
    // Gross Area = 50 m², Door = 0.9 m * 2.1 m = 1.89 m², Window = 1.2 m * 1 m = 1.2 m²
    // Total Openings = 3.09 m², Paintable Area = 46.91 m²
    // Coverage = 10 m²/L, Coats = 2, Waste = 10%
    // Base Paint = 9.382 L, Final Paint = 10.3202 L
    @Test
    fun testOpeningsCalculation() {
        val grossArea = BigDecimal("50")
        val doorArea = PaintCoverCalculator.calculateOpeningArea(BigDecimal("0.9"), BigDecimal("2.1"), mc)
        assertEquals(0, BigDecimal("1.89").compareTo(doorArea))

        val windowArea = PaintCoverCalculator.calculateOpeningArea(BigDecimal("1.2"), BigDecimal("1.0"), mc)
        assertEquals(0, BigDecimal("1.2").compareTo(windowArea))

        val totalOpening = doorArea.add(windowArea, mc)
        assertEquals(0, BigDecimal("3.09").compareTo(totalOpening))

        val paintableArea = PaintCoverCalculator.calculatePaintableArea(grossArea, totalOpening, mc)
        assertEquals(0, BigDecimal("46.91").compareTo(paintableArea))

        val basePaint = PaintCoverCalculator.calculatePaintRequired(
            paintableArea = paintableArea,
            coats = BigInteger.valueOf(2),
            coveragePerLiter = BigDecimal("10"),
            mathContext = mc
        )
        assertEquals(0, BigDecimal("9.382").compareTo(basePaint))

        val finalPaint = PaintCoverCalculator.calculatePaintWithWaste(
            paintRequired = basePaint,
            wastePercent = BigDecimal("10"),
            mathContext = mc
        )
        assertEquals(0, BigDecimal("10.3202").compareTo(finalPaint))
    }

    // Critical Test 8: Large Values without overflow
    @Test
    fun testLargeValues() {
        val largeArea = BigDecimal("100000000000000000000000") // 10^23
        val coverage = BigDecimal("10")
        val coats = BigInteger.valueOf(2)
        val paint = PaintCoverCalculator.calculatePaintRequired(largeArea, coats, coverage, mc)
        assertEquals(0, BigDecimal("20000000000000000000000").compareTo(paint))
    }

    // Critical Test 9: Very Small Values
    @Test
    fun testSmallValues() {
        val tinyArea = BigDecimal("0.000001")
        val coverage = BigDecimal("10")
        val coats = BigInteger.valueOf(1)
        val paint = PaintCoverCalculator.calculatePaintRequired(tinyArea, coats, coverage, mc)
        assertEquals(0, BigDecimal("0.0000001").compareTo(paint))
    }

    // BigInteger Ceiling Division precision
    @Test
    fun testCeilingDivisionPrecision() {
        val paintRequired = BigDecimal("10.0000000000000000000000001")
        val canSize = BigDecimal("5")
        val cans = PaintCoverCalculator.calculateCansRequired(paintRequired, canSize)
        assertEquals(BigInteger.valueOf(3), cans)
    }

    // Critical Test: No Openings
    // Gross Area = 50 m², Openings = none -> Paintable Area = 50 m²
    @Test
    fun testNoOpenings() {
        val grossArea = BigDecimal("50")
        val openingArea = BigDecimal.ZERO
        val paintableArea = PaintCoverCalculator.calculatePaintableArea(grossArea, openingArea, mc)
        assertEquals(0, BigDecimal("50").compareTo(paintableArea))
    }

    // Critical Test: BigDecimal No Floating-Point Artifacts
    @Test
    fun testBigDecimalNoFloatingPointArtifacts() {
        val a = BigDecimal("0.1")
        val b = BigDecimal("0.2")
        val sum = a.add(b, mc)
        assertEquals(BigDecimal("0.3"), sum)
        // Contrast with double where 0.1 + 0.2 != 0.3
        assertTrue(0.1 + 0.2 != 0.3)
    }

    // Critical Test: Leftover Paint Non-Negative
    @Test
    fun testLeftoverNonNegative() {
        val totalPurchased = BigDecimal("10")
        val paintRequired = BigDecimal("11")
        val leftover = PaintCoverCalculator.calculateLeftover(totalPurchased, paintRequired, mc)
        assertEquals(0, BigDecimal.ZERO.compareTo(leftover))
    }
}

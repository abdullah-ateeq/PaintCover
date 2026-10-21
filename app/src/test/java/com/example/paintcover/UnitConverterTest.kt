package com.example.paintcover

import com.example.paintcover.data.model.AreaUnit
import com.example.paintcover.data.model.CoverageUnit
import com.example.paintcover.data.model.LengthUnit
import com.example.paintcover.data.model.PaintVolumeUnit
import com.example.paintcover.domain.math.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

class UnitConverterTest {

    private val mc = PaintCoverCalculator.DEFAULT_MATH_CONTEXT

    @Test
    fun testLengthUnitConversions() {
        // 1 m = 1000 mm
        val m1 = LengthUnitConverter.toMeters(BigDecimal("1000"), LengthUnit.MILLIMETER, mc)
        assertEquals(0, BigDecimal.ONE.compareTo(m1))

        // 1 m = 100 cm
        val m2 = LengthUnitConverter.toMeters(BigDecimal("100"), LengthUnit.CENTIMETER, mc)
        assertEquals(0, BigDecimal.ONE.compareTo(m2))

        // 1 in = 0.0254 m
        val m3 = LengthUnitConverter.toMeters(BigDecimal("1"), LengthUnit.INCH, mc)
        assertEquals(0, BigDecimal("0.0254").compareTo(m3))

        // 1 ft = 0.3048 m
        val m4 = LengthUnitConverter.toMeters(BigDecimal("1"), LengthUnit.FOOT, mc)
        assertEquals(0, BigDecimal("0.3048").compareTo(m4))

        // 1 yd = 0.9144 m
        val m5 = LengthUnitConverter.toMeters(BigDecimal("1"), LengthUnit.YARD, mc)
        assertEquals(0, BigDecimal("0.9144").compareTo(m5))
    }

    @Test
    fun testAreaUnitConversions() {
        // 1 ft² = 0.09290304 m²
        val a1 = AreaUnitConverter.toSquareMeters(BigDecimal("1"), AreaUnit.SQUARE_FOOT, mc)
        assertEquals(0, BigDecimal("0.09290304").compareTo(a1))

        // 1 in² = 0.00064516 m²
        val a2 = AreaUnitConverter.toSquareMeters(BigDecimal("1"), AreaUnit.SQUARE_INCH, mc)
        assertEquals(0, BigDecimal("0.00064516").compareTo(a2))

        // 1 yd² = 0.83612736 m²
        val a3 = AreaUnitConverter.toSquareMeters(BigDecimal("1"), AreaUnit.SQUARE_YARD, mc)
        assertEquals(0, BigDecimal("0.83612736").compareTo(a3))
    }

    @Test
    fun testVolumeUnitConversions() {
        // 1 US gal = 3.785411784 L
        val v1 = PaintVolumeConverter.toLiters(BigDecimal("1"), PaintVolumeUnit.US_GALLON, mc)
        assertEquals(0, BigDecimal("3.785411784").compareTo(v1))

        // 1 US fl oz = 0.0295735295625 L
        val v2 = PaintVolumeConverter.toLiters(BigDecimal("1"), PaintVolumeUnit.US_FLUID_OUNCE, mc)
        assertEquals(0, BigDecimal("0.0295735295625").compareTo(v2))

        // 1000 mL = 1 L
        val v3 = PaintVolumeConverter.toLiters(BigDecimal("1000"), PaintVolumeUnit.MILLILITER, mc)
        assertEquals(0, BigDecimal.ONE.compareTo(v3))
    }

    // Critical Test: Mixed Area / Coverage Units
    // Area = 50 m², Coverage = 100 ft²/gal, Coats = 1, Waste = 0%
    @Test
    fun testMixedAreaCoverageUnits() {
        val areaM2 = BigDecimal("50")
        val coverageFt2Gal = BigDecimal("100")

        // 1 ft² = 0.09290304 m²
        // 1 gal = 3.785411784 L
        // Coverage in m²/L = 100 * (0.09290304 / 3.785411784) m²/L
        val coverageM2PerLiter = CoverageConverter.toSquareMetersPerLiter(
            coverage = coverageFt2Gal,
            unit = CoverageUnit.SQUARE_FOOT_PER_GALLON,
            mathContext = mc
        )

        val paintRequiredLiters = PaintCoverCalculator.calculatePaintRequired(
            paintableArea = areaM2,
            coats = BigInteger.ONE,
            coveragePerLiter = coverageM2PerLiter,
            mathContext = mc
        )

        // Expected: 50 / (100 * 0.09290304 / 3.785411784) = 50 * 3.785411784 / 9.290304 = 20.372916... L
        val expected = BigDecimal("50").multiply(BigDecimal("3.785411784"), mc)
            .divide(BigDecimal("9.290304"), mc)

        // Verify matches accurately to 20 decimal places (beyond precision setting of 12)
        assertEquals(
            0,
            expected.setScale(20, java.math.RoundingMode.HALF_UP)
                .compareTo(paintRequiredLiters.setScale(20, java.math.RoundingMode.HALF_UP))
        )
    }

    // Critical Test: Imperial
    // Area = 500 ft², Coverage = 350 ft²/gal, Coats = 2, Waste = 0%
    // In gallons: 500 * 2 / 350 = 1000 / 350 = 2.857142857... gal
    @Test
    fun testImperialCalculation() {
        val areaFt2 = BigDecimal("500")
        val coverageFt2PerGal = BigDecimal("350")
        val coats = BigInteger.valueOf(2)

        val areaM2 = AreaUnitConverter.toSquareMeters(areaFt2, AreaUnit.SQUARE_FOOT, mc)
        val coverageM2PerL = CoverageConverter.toSquareMetersPerLiter(coverageFt2PerGal, CoverageUnit.SQUARE_FOOT_PER_GALLON, mc)

        val paintLiters = PaintCoverCalculator.calculatePaintRequired(areaM2, coats, coverageM2PerL, mc)
        val paintGallons = PaintVolumeConverter.fromLiters(paintLiters, PaintVolumeUnit.US_GALLON, mc)

        val expectedGallons = BigDecimal("1000").divide(BigDecimal("350"), mc)
        assertEquals(
            0,
            expectedGallons.setScale(20, java.math.RoundingMode.HALF_UP)
                .compareTo(paintGallons.setScale(20, java.math.RoundingMode.HALF_UP))
        )
    }
}

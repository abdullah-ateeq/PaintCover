package com.example.paintcover

import com.example.paintcover.data.model.PaintCoverError
import com.example.paintcover.domain.math.PaintCoverParser
import com.example.paintcover.domain.math.PaintCoverValidator
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

class ValidatorAndParserTest {

    @Test
    fun testParserValidNumbers() {
        // Accept: 100, 100.5, .5, 10., 0.001, 1E+5, 1.5E-2
        val n1 = PaintCoverParser.parseNumber("100")
        assertTrue(n1.isSuccess)
        assertEquals(0, BigDecimal("100").compareTo(n1.getOrThrow()))

        val n2 = PaintCoverParser.parseNumber("100.5")
        assertTrue(n2.isSuccess)
        assertEquals(0, BigDecimal("100.5").compareTo(n2.getOrThrow()))

        val n3 = PaintCoverParser.parseNumber(".5")
        assertTrue(n3.isSuccess)
        assertEquals(0, BigDecimal("0.5").compareTo(n3.getOrThrow()))

        val n4 = PaintCoverParser.parseNumber("10.")
        assertTrue(n4.isSuccess)
        assertEquals(0, BigDecimal("10").compareTo(n4.getOrThrow()))

        val n5 = PaintCoverParser.parseNumber("0.001")
        assertTrue(n5.isSuccess)
        assertEquals(0, BigDecimal("0.001").compareTo(n5.getOrThrow()))

        val n6 = PaintCoverParser.parseNumber("1E+5")
        assertTrue(n6.isSuccess)
        assertEquals(0, BigDecimal("100000").compareTo(n6.getOrThrow()))

        val n7 = PaintCoverParser.parseNumber("1.5E-2")
        assertTrue(n7.isSuccess)
        assertEquals(0, BigDecimal("0.015").compareTo(n7.getOrThrow()))
    }

    @Test
    fun testParserInvalidNumbers() {
        // Reject: abc, 12..5, 1.2.3, --
        assertTrue(PaintCoverParser.parseNumber("abc").isFailure)
        assertTrue(PaintCoverParser.parseNumber("12..5").isFailure)
        assertTrue(PaintCoverParser.parseNumber("1.2.3").isFailure)
        assertTrue(PaintCoverParser.parseNumber("--").isFailure)
        assertTrue(PaintCoverParser.parseNumber("").isFailure)
    }

    @Test
    fun testParserMaxDigitsLength() {
        val validDigits = "1".repeat(500)
        assertTrue(PaintCoverParser.parseNumber(validDigits).isSuccess)

        val tooManyDigits = "1".repeat(501)
        val result = PaintCoverParser.parseNumber(tooManyDigits)
        assertTrue(result.isFailure)
        assertEquals("Number is too long. Maximum 500 digits.", result.exceptionOrNull()?.message)
    }

    @Test
    fun testValidateArea() {
        // Area > 0
        assertNull(PaintCoverValidator.validateArea(BigDecimal("50")))
        assertEquals(PaintCoverError.NonPositiveArea, PaintCoverValidator.validateArea(BigDecimal.ZERO))
        assertEquals(PaintCoverError.NonPositiveArea, PaintCoverValidator.validateArea(BigDecimal("-5")))
    }

    @Test
    fun testValidateDimension() {
        assertNull(PaintCoverValidator.validateDimension(BigDecimal("10")))
        assertEquals(PaintCoverError.NonPositiveDimension, PaintCoverValidator.validateDimension(BigDecimal.ZERO))
        assertEquals(PaintCoverError.NonPositiveDimension, PaintCoverValidator.validateDimension(BigDecimal("-1")))
    }

    @Test
    fun testValidateCoverage() {
        // Coverage > 0
        assertNull(PaintCoverValidator.validateCoverage(BigDecimal("10")))
        assertEquals(PaintCoverError.NonPositiveCoverage, PaintCoverValidator.validateCoverage(BigDecimal.ZERO))
        assertEquals(PaintCoverError.NonPositiveCoverage, PaintCoverValidator.validateCoverage(BigDecimal("-10")))
    }

    @Test
    fun testValidateCoats() {
        // Positive whole numbers only
        assertEquals(1, PaintCoverValidator.validateCoats("1").first)
        assertEquals(2, PaintCoverValidator.validateCoats("2").first)
        assertEquals(5, PaintCoverValidator.validateCoats("5").first)

        // Reject: 0, -1, 1.5, abc
        assertEquals(PaintCoverError.InvalidCoats, PaintCoverValidator.validateCoats("0").second)
        assertEquals(PaintCoverError.InvalidCoats, PaintCoverValidator.validateCoats("-1").second)
        assertEquals(PaintCoverError.InvalidCoats, PaintCoverValidator.validateCoats("1.5").second)
        assertEquals(PaintCoverError.InvalidCoats, PaintCoverValidator.validateCoats("abc").second)
    }

    @Test
    fun testValidateWastePercent() {
        // 0% to 100%
        assertNull(PaintCoverValidator.validateWastePercent(BigDecimal.ZERO))
        assertNull(PaintCoverValidator.validateWastePercent(BigDecimal("10")))
        assertNull(PaintCoverValidator.validateWastePercent(BigDecimal("100")))

        assertEquals(PaintCoverError.InvalidWastePercent, PaintCoverValidator.validateWastePercent(BigDecimal("-5")))
        assertEquals(PaintCoverError.InvalidWastePercent, PaintCoverValidator.validateWastePercent(BigDecimal("101")))
    }

    @Test
    fun testValidateCanSize() {
        assertNull(PaintCoverValidator.validateCanSize(null))
        assertNull(PaintCoverValidator.validateCanSize(BigDecimal("5")))
        assertEquals(PaintCoverError.NonPositiveCanSize, PaintCoverValidator.validateCanSize(BigDecimal.ZERO))
        assertEquals(PaintCoverError.NonPositiveCanSize, PaintCoverValidator.validateCanSize(BigDecimal("-5")))
    }

    @Test
    fun testValidateOpenings() {
        // Openings < Gross Area
        assertNull(PaintCoverValidator.validatePaintableArea(BigDecimal("50"), BigDecimal("4.02")))
        // Openings >= Gross Area
        assertEquals(PaintCoverError.OpeningTooLarge, PaintCoverValidator.validatePaintableArea(BigDecimal("50"), BigDecimal("50")))
        assertEquals(PaintCoverError.OpeningTooLarge, PaintCoverValidator.validatePaintableArea(BigDecimal("10"), BigDecimal("12")))
    }
}

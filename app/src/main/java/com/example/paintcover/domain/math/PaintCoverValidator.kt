package com.example.paintcover.domain.math

import com.example.paintcover.data.model.PaintCoverError
import java.math.BigDecimal

object PaintCoverValidator {

    fun validateArea(area: BigDecimal): PaintCoverError? {
        if (area <= BigDecimal.ZERO) {
            return PaintCoverError.NonPositiveArea
        }
        return null
    }

    fun validateDimension(dimension: BigDecimal): PaintCoverError? {
        if (dimension <= BigDecimal.ZERO) {
            return PaintCoverError.NonPositiveDimension
        }
        return null
    }

    fun validateCoverage(coverage: BigDecimal): PaintCoverError? {
        if (coverage <= BigDecimal.ZERO) {
            return PaintCoverError.NonPositiveCoverage
        }
        return null
    }

    fun validateCoats(coatsInput: String): Pair<Int?, PaintCoverError?> {
        val trimmed = coatsInput.trim()
        if (trimmed.isEmpty()) {
            return null to PaintCoverError.EmptyInput
        }
        // Check if it's an integer
        val intVal = trimmed.toIntOrNull()
        if (intVal == null || intVal <= 0) {
            return null to PaintCoverError.InvalidCoats
        }
        return intVal to null
    }

    fun validateWastePercent(waste: BigDecimal): PaintCoverError? {
        if (waste < BigDecimal.ZERO || waste > BigDecimal("100")) {
            return PaintCoverError.InvalidWastePercent
        }
        return null
    }

    fun validateCanSize(canSize: BigDecimal?): PaintCoverError? {
        if (canSize == null) return null
        if (canSize <= BigDecimal.ZERO) {
            return PaintCoverError.NonPositiveCanSize
        }
        return null
    }

    fun validatePaintableArea(grossArea: BigDecimal, openingArea: BigDecimal): PaintCoverError? {
        if (openingArea >= grossArea) {
            return PaintCoverError.OpeningTooLarge
        }
        return null
    }
}

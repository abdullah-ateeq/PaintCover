package com.example.paintcover.domain.math

import java.math.BigDecimal

object PaintCoverParser {
    const val MAX_DIGITS = 500

    fun parseNumber(input: String): Result<BigDecimal> {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Input cannot be empty."))
        }

        // Count digits
        val digitCount = trimmed.count { it.isDigit() }
        if (digitCount > MAX_DIGITS) {
            return Result.failure(IllegalArgumentException("Number is too long. Maximum 500 digits."))
        }

        // Normalize leading or trailing decimal point
        val normalized = when {
            trimmed.startsWith(".") -> "0$trimmed"
            trimmed.startsWith("-.") -> "-0${trimmed.substring(1)}"
            trimmed.startsWith("+.") -> "+0${trimmed.substring(1)}"
            trimmed.endsWith(".") -> trimmed.dropLast(1)
            else -> trimmed
        }

        return try {
            val bd = BigDecimal(normalized)
            Result.success(bd)
        } catch (e: Exception) {
            Result.failure(IllegalArgumentException("Enter a valid number."))
        }
    }
}

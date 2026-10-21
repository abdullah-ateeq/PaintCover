package com.example.paintcover.domain.math

import com.example.paintcover.data.model.DisplayRoundingMode
import com.example.paintcover.data.model.PaintCoverResult
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

object PaintCoverFormatter {

    fun formatBigDecimal(
        value: BigDecimal,
        decimalPlaces: Int = 4,
        roundingMode: DisplayRoundingMode = DisplayRoundingMode.HALF_UP
    ): String {
        val rounded = if (decimalPlaces == 0) {
            value.setScale(0, roundingMode.mode)
        } else {
            value.setScale(decimalPlaces, roundingMode.mode)
        }

        // Strip trailing zeros after decimal point
        val stripped = rounded.stripTrailingZeros()
        // If scale is negative (e.g. 100 with scale -2), set scale to 0 to prevent scientific notation like 1E+2
        val plain = if (stripped.scale() < 0) {
            stripped.setScale(0)
        } else {
            stripped
        }

        return plain.toPlainString()
    }

    fun formatWholeNumber(value: BigInteger): String {
        return value.toString()
    }

    fun formatSummaryForShare(result: PaintCoverResult, decimalPlaces: Int = 4, roundingMode: DisplayRoundingMode = DisplayRoundingMode.HALF_UP): String {
        val sb = StringBuilder()
        sb.appendLine("PaintCover")
        sb.appendLine()
        sb.appendLine("Surface: ${result.surfaceType.displayName.lowercase().replaceFirstChar { it.uppercase() }}")
        sb.appendLine()
        if (result.openingArea > BigDecimal.ZERO) {
            sb.appendLine("Gross Surface: ${formatBigDecimal(result.grossSurfaceArea, decimalPlaces, roundingMode)} m²")
            sb.appendLine("Openings: ${formatBigDecimal(result.openingArea, decimalPlaces, roundingMode)} m²")
        }
        sb.appendLine("Paintable Area: ${formatBigDecimal(result.paintableArea, decimalPlaces, roundingMode)} m²")
        sb.appendLine()
        sb.appendLine("Coverage: ${formatBigDecimal(result.coveragePerLiter, decimalPlaces, roundingMode)} m²/L")
        sb.appendLine("Coats: ${result.coats}")
        sb.appendLine("Waste: ${formatBigDecimal(result.wastePercent, decimalPlaces, roundingMode)}%")
        sb.appendLine()
        sb.appendLine("Paint Required: ${formatBigDecimal(result.paintRequired, decimalPlaces, roundingMode)} L")

        if (result.canSize != null && result.cansRequired != null && result.totalPurchasedPaint != null && result.leftoverPaint != null) {
            sb.appendLine()
            sb.appendLine("Can Size: ${formatBigDecimal(result.canSize, decimalPlaces, roundingMode)} L")
            sb.appendLine("Cans Required: ${formatWholeNumber(result.cansRequired)}")
            sb.appendLine("Total Purchased: ${formatBigDecimal(result.totalPurchasedPaint, decimalPlaces, roundingMode)} L")
            sb.appendLine("Estimated Leftover: ${formatBigDecimal(result.leftoverPaint, decimalPlaces, roundingMode)} L")
        }

        return sb.toString().trimEnd()
    }
}

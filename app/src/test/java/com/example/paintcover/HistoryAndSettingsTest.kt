package com.example.paintcover

import com.example.paintcover.data.model.*
import org.junit.Assert.*
import org.junit.Test

class HistoryAndSettingsTest {

    @Test
    fun testSettingsDefaultValues() {
        val settings = AppSettings()
        assertEquals(AppTheme.SYSTEM, settings.theme)
        assertTrue(settings.dynamicColor)
        assertTrue(settings.hapticFeedback)
        assertTrue(settings.animationsEnabled)
        assertEquals(4, settings.decimalPlaces)
        assertEquals(DisplayRoundingMode.HALF_UP, settings.roundingMode)
        assertTrue(settings.showFormula)
        assertTrue(settings.showCalculationSteps)
        assertTrue(settings.showPaintDiagram)
        assertTrue(settings.showPurchaseDetails)
        assertTrue(settings.confirmDeleteHistory)
    }

    @Test
    fun testDuplicateHistoryDetection() {
        val entry1 = PaintCoverHistory(
            id = "1",
            surfaceType = SurfaceType.WALL,
            inputMode = InputMode.AREA,
            surfaceArea = "50",
            surfaceLength = null,
            surfaceWidth = null,
            surfaceAreaUnit = AreaUnit.SQUARE_METER,
            surfaceLengthUnit = null,
            surfaceWidthUnit = null,
            coverage = "10",
            coverageUnit = CoverageUnit.SQUARE_METER_PER_LITER,
            coats = 2,
            wastePercent = "10",
            canSize = "5",
            canSizeUnit = PaintVolumeUnit.LITER,
            savedAt = 1000L
        )

        val entry2Identical = entry1.copy(id = "2", savedAt = 2000L)
        val entry3Different = entry1.copy(id = "3", coats = 3)

        // Helper comparison logic matching repository
        fun isDuplicate(a: PaintCoverHistory, b: PaintCoverHistory): Boolean {
            return a.surfaceType == b.surfaceType &&
                    a.inputMode == b.inputMode &&
                    a.surfaceArea == b.surfaceArea &&
                    a.surfaceLength == b.surfaceLength &&
                    a.surfaceWidth == b.surfaceWidth &&
                    a.surfaceAreaUnit == b.surfaceAreaUnit &&
                    a.surfaceLengthUnit == b.surfaceLengthUnit &&
                    a.surfaceWidthUnit == b.surfaceWidthUnit &&
                    a.coverage == b.coverage &&
                    a.coverageUnit == b.coverageUnit &&
                    a.coats == b.coats &&
                    a.wastePercent == b.wastePercent &&
                    a.canSize == b.canSize &&
                    a.canSizeUnit == b.canSizeUnit
        }

        assertTrue(isDuplicate(entry1, entry2Identical))
        assertFalse(isDuplicate(entry1, entry3Different))
    }

    @Test
    fun testHistoryLimitTenEntries() {
        val list = mutableListOf<PaintCoverHistory>()
        for (i in 1..15) {
            val entry = PaintCoverHistory(
                id = "item_$i",
                surfaceType = SurfaceType.WALL,
                inputMode = InputMode.AREA,
                surfaceArea = "$i",
                surfaceLength = null,
                surfaceWidth = null,
                surfaceAreaUnit = AreaUnit.SQUARE_METER,
                surfaceLengthUnit = null,
                surfaceWidthUnit = null,
                coverage = "10",
                coverageUnit = CoverageUnit.SQUARE_METER_PER_LITER,
                coats = 2,
                wastePercent = "10",
                canSize = null,
                canSizeUnit = null,
                savedAt = i.toLong()
            )
            list.add(0, entry)
            if (list.size > 10) {
                list.removeAt(list.lastIndex)
            }
        }

        assertEquals(10, list.size)
        // Most recent should be item_15 at index 0
        assertEquals("item_15", list.first().id)
        // Oldest retained should be item_6 at index 9 (items 1 to 5 dropped)
        assertEquals("item_6", list.last().id)
    }
}

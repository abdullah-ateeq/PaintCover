package com.example.paintcover.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.paintcover.data.model.*
import org.json.JSONArray
import org.json.JSONObject

class HistoryRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("paint_cover_history", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_HISTORY_LIST = "key_history_list"
        const val MAX_ENTRIES = 10
    }

    fun getHistory(): List<PaintCoverHistory> {
        val jsonString = prefs.getString(KEY_HISTORY_LIST, null) ?: return emptyList()
        val list = mutableListOf<PaintCoverHistory>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                list.add(
                    PaintCoverHistory(
                        id = obj.getString("id"),
                        surfaceType = SurfaceType.valueOf(obj.getString("surfaceType")),
                        inputMode = InputMode.valueOf(obj.getString("inputMode")),
                        surfaceArea = obj.optString("surfaceArea").takeIf { it.isNotEmpty() },
                        surfaceLength = obj.optString("surfaceLength").takeIf { it.isNotEmpty() },
                        surfaceWidth = obj.optString("surfaceWidth").takeIf { it.isNotEmpty() },
                        surfaceAreaUnit = obj.optString("surfaceAreaUnit").takeIf { it.isNotEmpty() }?.let { AreaUnit.valueOf(it) },
                        surfaceLengthUnit = obj.optString("surfaceLengthUnit").takeIf { it.isNotEmpty() }?.let { LengthUnit.valueOf(it) },
                        surfaceWidthUnit = obj.optString("surfaceWidthUnit").takeIf { it.isNotEmpty() }?.let { LengthUnit.valueOf(it) },
                        coverage = obj.getString("coverage"),
                        coverageUnit = CoverageUnit.valueOf(obj.getString("coverageUnit")),
                        coats = obj.getInt("coats"),
                        wastePercent = obj.getString("wastePercent"),
                        canSize = obj.optString("canSize").takeIf { it.isNotEmpty() },
                        canSizeUnit = obj.optString("canSizeUnit").takeIf { it.isNotEmpty() }?.let { PaintVolumeUnit.valueOf(it) },
                        savedAt = obj.getLong("savedAt"),
                        calculatedPaintRequiredDisplay = obj.optString("calculatedPaintRequiredDisplay"),
                        paintableAreaDisplay = obj.optString("paintableAreaDisplay")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun isDuplicate(latest: PaintCoverHistory?, newEntry: PaintCoverHistory): Boolean {
        if (latest == null) return false
        return latest.surfaceType == newEntry.surfaceType &&
                latest.inputMode == newEntry.inputMode &&
                latest.surfaceArea == newEntry.surfaceArea &&
                latest.surfaceLength == newEntry.surfaceLength &&
                latest.surfaceWidth == newEntry.surfaceWidth &&
                latest.surfaceAreaUnit == newEntry.surfaceAreaUnit &&
                latest.surfaceLengthUnit == newEntry.surfaceLengthUnit &&
                latest.surfaceWidthUnit == newEntry.surfaceWidthUnit &&
                latest.coverage == newEntry.coverage &&
                latest.coverageUnit == newEntry.coverageUnit &&
                latest.coats == newEntry.coats &&
                latest.wastePercent == newEntry.wastePercent &&
                latest.canSize == newEntry.canSize &&
                latest.canSizeUnit == newEntry.canSizeUnit
    }

    fun addEntry(entry: PaintCoverHistory): List<PaintCoverHistory> {
        val current = getHistory().toMutableList()
        // If the consecutive latest is identical, don't duplicate
        if (current.isNotEmpty() && isDuplicate(current.first(), entry)) {
            return current
        }

        // Add to front (newest first)
        current.add(0, entry)

        // Limit to MAX_ENTRIES (10). If 11th added, remove oldest
        while (current.size > MAX_ENTRIES) {
            current.removeAt(current.lastIndex)
        }

        saveList(current)
        return current
    }

    fun deleteEntry(id: String): List<PaintCoverHistory> {
        val current = getHistory().toMutableList()
        current.removeAll { it.id == id }
        saveList(current)
        return current
    }

    fun clearAll(): List<PaintCoverHistory> {
        prefs.edit().remove(KEY_HISTORY_LIST).apply()
        return emptyList()
    }

    private fun saveList(list: List<PaintCoverHistory>) {
        val jsonArray = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("surfaceType", item.surfaceType.name)
            obj.put("inputMode", item.inputMode.name)
            obj.put("surfaceArea", item.surfaceArea ?: "")
            obj.put("surfaceLength", item.surfaceLength ?: "")
            obj.put("surfaceWidth", item.surfaceWidth ?: "")
            obj.put("surfaceAreaUnit", item.surfaceAreaUnit?.name ?: "")
            obj.put("surfaceLengthUnit", item.surfaceLengthUnit?.name ?: "")
            obj.put("surfaceWidthUnit", item.surfaceWidthUnit?.name ?: "")
            obj.put("coverage", item.coverage)
            obj.put("coverageUnit", item.coverageUnit.name)
            obj.put("coats", item.coats)
            obj.put("wastePercent", item.wastePercent)
            obj.put("canSize", item.canSize ?: "")
            obj.put("canSizeUnit", item.canSizeUnit?.name ?: "")
            obj.put("savedAt", item.savedAt)
            obj.put("calculatedPaintRequiredDisplay", item.calculatedPaintRequiredDisplay)
            obj.put("paintableAreaDisplay", item.paintableAreaDisplay)
            jsonArray.put(obj)
        }
        prefs.edit().putString(KEY_HISTORY_LIST, jsonArray.toString()).apply()
    }
}

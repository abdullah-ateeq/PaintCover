package com.example.paintcover.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.paintcover.data.repository.HistoryRepository
import com.example.paintcover.data.repository.SettingsRepository

class PaintCoverViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaintCoverViewModel::class.java)) {
            val settingsRepo = SettingsRepository(context.applicationContext)
            val historyRepo = HistoryRepository(context.applicationContext)
            return PaintCoverViewModel(settingsRepo, historyRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

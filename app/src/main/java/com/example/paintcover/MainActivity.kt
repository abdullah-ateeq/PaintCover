package com.example.paintcover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.paintcover.ui.PaintCoverApp
import com.example.paintcover.ui.PaintCoverViewModel
import com.example.paintcover.ui.PaintCoverViewModelFactory
import com.example.paintcover.ui.theme.PaintCoverTheme

class MainActivity : ComponentActivity() {

    private val viewModel: PaintCoverViewModel by viewModels {
        PaintCoverViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsState()
            PaintCoverTheme(
                appTheme = uiState.settings.theme,
                dynamicColor = uiState.settings.dynamicColor
            ) {
                PaintCoverApp(viewModel = viewModel)
            }
        }
    }
}

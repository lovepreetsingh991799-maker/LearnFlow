package com.noisefit.smartlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.noisefit.smartlearning.ui.SmartLearningApp
import com.noisefit.smartlearning.ui.theme.SmartLearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartLearningTheme {
                SmartLearningApp()
            }
        }
    }
}

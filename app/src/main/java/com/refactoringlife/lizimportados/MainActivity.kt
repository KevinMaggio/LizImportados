package com.refactoringlife.lizimportados

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.refactoringlife.lizimportados.features.composablesLipsy.BottomBarItem
import com.refactoringlife.lizimportados.features.composablesLipsy.LipsyBottomBar
import com.refactoringlife.lizimportados.ui.theme.LizImportadosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LizImportadosTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        LipsyBottomBar(
                            goToCart = {},
                            goToHome = {},
                            goToContent = {},
                            selected = BottomBarItem.HOME
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}
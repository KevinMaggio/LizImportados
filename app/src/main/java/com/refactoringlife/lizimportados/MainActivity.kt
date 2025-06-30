package com.refactoringlife.lizimportados

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.features.bottomBar.LipsyBottomBar
import com.refactoringlife.lizimportados.ui.theme.LizImportadosTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.navigation.compose.rememberNavController
import com.refactoringlife.lizimportados.core.navigator.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LizImportadosTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        LipsyBottomBar(
                            goTo = { destination -> navController.navigate(destination) },
                            modifier = Modifier.navigationBarsPadding()
                        )
                    }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier
                            .navigationBarsPadding()
                    )
                }
            }
        }
    }
}
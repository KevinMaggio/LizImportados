package com.refactoringlife.lizimportados

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.refactoringlife.lizimportados.core.navigator.AppNavHost
import com.refactoringlife.lizimportados.core.navigator.AppRoutes
import com.refactoringlife.lizimportados.features.bottomBar.LipsyBottomBar
import com.refactoringlife.lizimportados.features.login.presenter.viewmodel.LoginViewModel
import com.refactoringlife.lizimportados.ui.theme.LizImportadosTheme
import androidx.compose.foundation.layout.navigationBarsPadding

class MainActivity : ComponentActivity() {
    
    private val loginViewModel: LoginViewModel by lazy { LoginViewModel() }
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            loginViewModel.handleSignInResult(result.data)
        } else {
            loginViewModel.handleSignInResult(null)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LizImportadosTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != AppRoutes.LOGIN) {
                            LipsyBottomBar(
                                goTo = { destination -> navController.navigate(destination) },
                                modifier = Modifier.navigationBarsPadding()
                            )
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.navigationBarsPadding(),
                        onGoogleSignInClick = { intent ->
                            googleSignInLauncher.launch(intent)
                        },
                        loginViewModel = loginViewModel
                    )
                }
            }
        }
    }
}
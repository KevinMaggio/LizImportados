package com.refactoringlife.lizimportadosv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.refactoringlife.lizimportadosv2.core.navigator.AppNavHost
import com.refactoringlife.lizimportadosv2.core.navigator.AppRoutes
import com.refactoringlife.lizimportadosv2.features.bottomBar.LipsyBottomBar
import com.refactoringlife.lizimportadosv2.features.login.presenter.viewmodel.LoginViewModel
import com.refactoringlife.lizimportadosv2.ui.theme.LizImportadosTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.core.navigator.navigateFromBottomBar
import androidx.core.view.WindowCompat
import com.refactoringlife.lizimportadosv2.core.auth.AuthStateViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by lazy { LoginViewModel() }
    private val authStateViewModel: AuthStateViewModel by lazy { AuthStateViewModel() }

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

        setupSystemBars()

        setContent {
            LizImportadosTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBottomBar = currentRoute != null && currentRoute != AppRoutes.LOGIN &&
                        currentRoute != AppRoutes.CART

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            LipsyBottomBar(
                                goTo = { destination ->
                                    navController.navigateFromBottomBar(destination)
                                },
                                modifier = Modifier.navigationBarsPadding()
                            )
                        }
                    },
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier
                            .navigationBarsPadding(),
                        onGoogleSignInClick = { intent ->
                            googleSignInLauncher.launch(intent)
                        },
                        loginViewModel = loginViewModel,
                        authStateViewModel = authStateViewModel
                    )
                }
            }
        }
    }

    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
package com.refactoringlife.lizimportadosv2.features.home.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyLoading
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportadosv2.core.navigator.DETAILS
import com.refactoringlife.lizimportadosv2.features.home.presenter.viewmodel.HomeViewModel
import com.refactoringlife.lizimportadosv2.features.home.presenter.views.HomeDataView
import com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel.CartViewModel
import com.refactoringlife.lizimportadosv2.core.auth.AuthStateViewModel
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
) {
    val viewModel: HomeViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val authStateViewModel: AuthStateViewModel = viewModel()
    val uiState by viewModel.state.collectAsState()
    val userEmail by authStateViewModel.userEmail.collectAsState()
    val cartState by cartViewModel.state.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.showLoading -> {
                LipsyLoading(
                    modifier = Modifier.fillMaxSize()
                )
            }
            uiState.showError -> {
                // TODO: Mostrar error
            }
            else -> {
                uiState.config?.let {
                    HomeDataView(
                        modifier = modifier,
                        state = uiState,
                        action = { category, id -> 
                            val finalId = if (id.isEmpty()) "empty" else id
                            navController.navigate("$DETAILS/$category/$finalId") 
                        },
                        onAddComboToCart = { comboId ->
                            userEmail?.let { email ->
                                cartViewModel.addComboToCart(email, comboId)
                            }
                        }
                    )
                }
            }
        }
        
        // Snackbar para notificaciones
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
    }
    
    // Mostrar Snackbar cuando hay mensaje
    LaunchedEffect(cartState.addToCartMessage) {
        cartState.addToCartMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            // Limpiar el mensaje después de mostrarlo
            cartViewModel.clearAddToCartMessage()
        }
    }
}
package com.refactoringlife.lizimportadosv2.features.man.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportadosv2.core.navigator.MAN
import com.refactoringlife.lizimportadosv2.core.navigator.navigateToDetails
import com.refactoringlife.lizimportadosv2.features.man.presenter.viewmodel.ManViewModel
import com.refactoringlife.lizimportadosv2.features.man.presenter.views.ManDataView
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyLoading
import com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel.CartViewModel
import androidx.compose.foundation.layout.padding
import com.refactoringlife.lizimportadosv2.core.auth.AuthStateViewModel

@Composable
fun ManScreen(
    navController: NavHostController
) {
    val viewModel: ManViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val authStateViewModel: AuthStateViewModel = viewModel()
    val uiState by viewModel.state.collectAsState()
    val cartState by cartViewModel.state.collectAsState()
    val userEmail by authStateViewModel.userEmail.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    var addingToCartProductId by remember { mutableStateOf<String?>(null) }

    // Resetear el estado de loading cuando termina la operación
    LaunchedEffect(cartState.isAddingToCart) {
        if (!cartState.isAddingToCart) {
            addingToCartProductId = null
        }
    }

    // Mostrar Snackbar cuando hay mensaje del carrito
    LaunchedEffect(cartState.addToCartMessage) {
        cartState.addToCartMessage?.let { message ->
            try {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = androidx.compose.material3.SnackbarDuration.Short
                )
            } catch (e: Exception) {
                // Log error if needed
            }
            cartViewModel.clearAddToCartMessage()
        }
    }
    
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
                uiState.products?.let { products ->
                    ManDataView(
                        products = products,
                        action = { id -> navController.navigateToDetails(category = MAN, id = id) },
                        addCartProduct = { productId ->
                            addingToCartProductId = productId
                            userEmail?.let { email ->
                                cartViewModel.addToCart(email, productId)
                            }
                        },
                        addingToCartProductId = addingToCartProductId
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
}

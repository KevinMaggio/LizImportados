package com.refactoringlife.lizimportadosv2.features.cart.presenter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel.CartViewModel
import com.refactoringlife.lizimportadosv2.features.cart.presenter.views.CartDataView
import com.refactoringlife.lizimportadosv2.core.auth.AuthStateViewModel

@Composable
fun CartScreen(authStateViewModel: AuthStateViewModel) {
    val cartViewModel: CartViewModel = viewModel()
    val state by cartViewModel.state.collectAsState()
    val userEmail by authStateViewModel.userEmail.collectAsState()

    LaunchedEffect(Unit) {
        userEmail?.let { email ->
            cartViewModel.loadCart(email)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            state.error != null -> {
                Text(
                    text = state.error!!,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                CartDataView(
                    product = state.cart,
                    onRemoveItem = { productId ->
                        userEmail?.let { email ->
                            cartViewModel.removeFromCart(email, productId)
                        }
                    },
                    onClearCart = {
                        userEmail?.let { email ->
                            cartViewModel.clearCart(email)
                        }
                    }
                )
            }
        }
    }
}
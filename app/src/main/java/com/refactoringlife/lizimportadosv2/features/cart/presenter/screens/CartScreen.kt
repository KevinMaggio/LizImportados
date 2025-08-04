package com.refactoringlife.lizimportadosv2.features.cart.presenter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel.CartViewModel
import com.refactoringlife.lizimportadosv2.features.cart.presenter.views.CartDataView
import com.refactoringlife.lizimportadosv2.ui.theme.CardBackGround
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue

@Composable
fun CartScreen() {
    val viewModel: CartViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // TODO: Obtener el email del usuario autenticado
    val userEmail = "usuario@ejemplo.com" // Esto debería venir del sistema de autenticación

    LaunchedEffect(userEmail) {
        viewModel.loadCart(userEmail)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBackGround)
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center),
                    color = TextBlue
                )
            }
            state.error != null -> {
                Text(
                    text = state.error!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
            else -> {
                CartDataView(
                    product = state.cart,
                    onRemoveItem = { productId ->
                        viewModel.removeFromCart(userEmail, productId)
                    },
                    onClearCart = {
                        viewModel.clearCart(userEmail)
                    }
                )
            }
        }
    }
}
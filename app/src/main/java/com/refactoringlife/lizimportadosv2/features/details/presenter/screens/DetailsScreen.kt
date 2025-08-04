package com.refactoringlife.lizimportadosv2.features.details.presenter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.utils.EMPTY
import com.refactoringlife.lizimportadosv2.core.utils.isTrue
import com.refactoringlife.lizimportadosv2.core.utils.isValid
import com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel.DetailsViewModel
import com.refactoringlife.lizimportadosv2.features.details.presenter.views.DetailsDataView
import com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel.CartViewModel
import android.util.Log

@Composable
fun DetailsScreen(
    category: String?,
    id: String?
) {
    val detailsViewModel: DetailsViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val detailsState by detailsViewModel.state.collectAsState()
    val cartState by cartViewModel.state.collectAsState()
    
    var showSuccessMessage by remember { mutableStateOf(false) }
    var lastCartSize by remember { mutableStateOf(0) }

    // TODO: Obtener el email del usuario autenticado
    val userEmail = "usuario@ejemplo.com" // Esto debería venir del sistema de autenticación

    LaunchedEffect(id, category) {
        Log.d("DetailsScreen", "🎯 Navegación recibida - Category: '$category', ID: '$id'")
        
        if (id.isNullOrEmpty() || id == EMPTY || id == "empty") {
            Log.d("DetailsScreen", "📋 Cargando por categoría: '$category'")
            category?.let { cat ->
                detailsViewModel.loadProductsByCategory(cat)
            }
        } else {
            Log.d("DetailsScreen", "🆔 Cargando por ID: '$id'")
            detailsViewModel.loadProductDetails(id)
        }
    }

    // Mostrar mensaje de éxito cuando se agrega al carrito
    LaunchedEffect(cartState.cart.products.size) {
        if (cartState.cart.products.size > lastCartSize && lastCartSize > 0) {
            showSuccessMessage = true
        }
        lastCartSize = cartState.cart.products.size
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            detailsState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            detailsState.error?.isNotEmpty() == true -> {
                Text(
                    text = detailsState.error!!,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            detailsState.mainProduct != null -> {
                val allProducts = listOf(detailsState.mainProduct!!) + detailsState.relatedProducts
                DetailsDataView(
                    products = allProducts,
                    onProductPageChanged = detailsViewModel::onProductPageChanged,
                    onAddToCart = { productId ->
                        Log.d("DetailsScreen", "🛒 Agregando producto $productId al carrito")
                        cartViewModel.addToCart(userEmail, productId)
                    }
                )
            }
            detailsState.relatedProducts.isNotEmpty() -> {
                DetailsDataView(
                    products = detailsState.relatedProducts,
                    onProductPageChanged = detailsViewModel::onProductPageChanged,
                    onAddToCart = { productId ->
                        Log.d("DetailsScreen", "🛒 Agregando producto $productId al carrito")
                        cartViewModel.addToCart(userEmail, productId)
                    }
                )
            }
            else -> {
                Text(
                    text = stringResource(R.string.no_product),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        // Snackbar de éxito
        if (showSuccessMessage) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    Text(
                        text = "✓ Producto agregado al carrito",
                        color = Color.White
                    )
                }
            ) {
                // Auto-hide después de 3 segundos
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(3000)
                    showSuccessMessage = false
                }
            }
        }
    }
}

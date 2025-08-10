package com.refactoringlife.lizimportadosv2.features.details.presenter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyLoading
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyProductNotFound
import com.refactoringlife.lizimportadosv2.core.utils.EMPTY
import com.refactoringlife.lizimportadosv2.core.utils.isTrue
import com.refactoringlife.lizimportadosv2.core.utils.isValid
import com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel.DetailsViewModel
import com.refactoringlife.lizimportadosv2.features.details.presenter.views.DetailsDataView
import com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel.CartViewModel
import com.refactoringlife.lizimportadosv2.core.auth.AuthStateViewModel
import android.util.Log
import kotlinx.coroutines.delay

@Composable
fun DetailsScreen(
    category: String?,
    id: String?,
    authStateViewModel: AuthStateViewModel,
    onNavigateBack: (() -> Unit)? = null
) {
    val detailsViewModel: DetailsViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val detailsState by detailsViewModel.state.collectAsState()
    val cartState by cartViewModel.state.collectAsState()
    val userEmail by authStateViewModel.userEmail.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }

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

    LaunchedEffect(cartState.addToCartMessage) {
        cartState.addToCartMessage?.let { message ->
            try {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = androidx.compose.material3.SnackbarDuration.Short
                )
            } catch (e: Exception) {
                Log.e("DetailsScreen", "❌ Error mostrando snackbar", e)
            }
            cartViewModel.clearAddToCartMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            detailsState.isLoading -> {
                LipsyLoading(
                    modifier = Modifier.fillMaxSize()
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
                        userEmail?.let { email ->
                            cartViewModel.addToCart(email, productId)
                        }
                    },
                    isAddingToCart = cartState.isAddingToCart
                )
            }
            detailsState.relatedProducts.isNotEmpty() -> {
                DetailsDataView(
                    products = detailsState.relatedProducts,
                    onProductPageChanged = detailsViewModel::onProductPageChanged,
                    onAddToCart = { productId ->
                        userEmail?.let { email ->
                            cartViewModel.addToCart(email, productId)
                        }
                    },
                    isAddingToCart = cartState.isAddingToCart
                )
            }
            else -> {
                LipsyProductNotFound(
                    onGoBack = onNavigateBack ?: {}
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp, start = 16.dp, end = 16.dp)
        )
    }
}

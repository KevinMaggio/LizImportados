package com.refactoringlife.lizimportadosv2.features.details.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.refactoringlife.lizimportadosv2.core.utils.isFalse
import com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel.DetailsViewModel
import com.refactoringlife.lizimportadosv2.features.details.presenter.views.DetailsDataView

@Composable
fun DetailsScreen(
    category: String?, 
    id: String?
) {
    val viewModel: DetailsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    
    // Cargar datos cuando cambie el ID o categoría
    LaunchedEffect(id, category) {
        if (id.isNullOrEmpty() || id == "empty") {
            category?.let { cat ->
                viewModel.loadProductsByCategory(cat)
            }
        } else {
            viewModel.loadProductDetails(id)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
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
            state.mainProduct != null -> {
                val allProducts = listOf(state.mainProduct!!) + state.relatedProducts
                DetailsDataView(products = allProducts)
            }
            state.relatedProducts.isNotEmpty() -> {
                DetailsDataView(products = state.relatedProducts)
            }
            else -> {
                Text(
                    text = "No se encontraron productos para esta categoría",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

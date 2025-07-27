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
    filter: String?, 
    id: String?
) {
    val viewModel: DetailsViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    
    // Cargar datos cuando cambie el ID
    LaunchedEffect(id) {
        id?.let { productId ->
            viewModel.loadProductDetails(productId)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        state.isLoading.isFalse {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        state.error?.let {
            Text(
                text = state.error ?: "Error desconocido",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        
        state.mainProduct?.let { product ->
            // Crear lista con producto principal + productos relacionados
            val allProducts = listOf(product) + state.relatedProducts
            
            DetailsDataView(
                products = allProducts
            )
        }
    }
}

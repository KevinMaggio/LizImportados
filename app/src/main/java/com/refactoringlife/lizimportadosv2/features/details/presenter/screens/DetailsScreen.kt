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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.utils.EMPTY
import com.refactoringlife.lizimportadosv2.core.utils.isTrue
import com.refactoringlife.lizimportadosv2.core.utils.isValid
import com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel.DetailsViewModel
import com.refactoringlife.lizimportadosv2.features.details.presenter.views.DetailsDataView

@Composable
fun DetailsScreen(
    category: String?,
    id: String?
) {
    val viewModel: DetailsViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(id, category) {
        if (id.isNullOrEmpty() || id == EMPTY) {
            category?.let { cat ->
                viewModel.loadProductsByCategory(cat)
            }
        } else {
            viewModel.loadProductDetails(id)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        state.isLoading.isTrue {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        state.error?.isValid { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        state.mainProduct?.let {
            val allProducts = listOf(state.mainProduct!!) + state.relatedProducts
            DetailsDataView(products = allProducts)
        }
        if (state.relatedProducts.isNotEmpty()) {
            DetailsDataView(products = state.relatedProducts)
        } else {
            Text(
                text = stringResource(R.string.no_product),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

package com.refactoringlife.lizimportados.features.woman.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.navigator.WOMAN
import com.refactoringlife.lizimportados.core.navigator.navigateToDetails
import com.refactoringlife.lizimportados.features.woman.presenter.viewmodel.WomanViewModel
import com.refactoringlife.lizimportados.features.woman.presenter.views.WomanDataView

@Composable
fun WomanScreen(
    navController: NavHostController
) {
    val viewModel : WomanViewModel = viewModel()

    val uiState by viewModel.state.collectAsState()
    
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.showLoading -> {
                // TODO: Mostrar loading
            }
            uiState.showError -> {
                // TODO: Mostrar error
            }
            else -> {
                uiState.products?.let { products ->
                    WomanDataView(
                        products = products,
                        action = { id -> navController.navigateToDetails(filter = WOMAN, id = id) }
                    )
                }
            }
        }
    }
}
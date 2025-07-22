package com.refactoringlife.lizimportados.features.man.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.navigator.MAN
import com.refactoringlife.lizimportados.core.navigator.navigateToDetails
import com.refactoringlife.lizimportados.features.man.presenter.viewmodel.ManViewModel
import com.refactoringlife.lizimportados.features.man.presenter.views.ManDataView

@Composable
fun ManScreen(
    navController: NavHostController
) {
    val viewModel : ManViewModel = viewModel()
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
                    ManDataView(
                        products = products,
                        action = { id -> navController.navigateToDetails(filter = MAN, id = id) }
                    )
                }
            }
        }
    }
}

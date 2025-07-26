package com.refactoringlife.lizimportadosv2.features.children.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportadosv2.core.navigator.CHILDREN
import com.refactoringlife.lizimportadosv2.core.navigator.navigateToDetails
import com.refactoringlife.lizimportadosv2.features.children.presenter.viewmodel.ChildrenViewModel
import com.refactoringlife.lizimportadosv2.features.children.presenter.views.ChildrenDataView

@Composable
fun ChildrenScreen(
    navController: NavHostController
) {
    val viewModel : ChildrenViewModel = viewModel()
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
                    ChildrenDataView(
                        products = products,
                        goToOptionScreen= { id -> navController.navigateToDetails(filter = CHILDREN, id = id) }
                    )
                }
            }
        }
    }
}
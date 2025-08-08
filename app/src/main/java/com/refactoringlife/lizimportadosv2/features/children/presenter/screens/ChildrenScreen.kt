package com.refactoringlife.lizimportadosv2.features.children.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyLoading
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    val viewModel: ChildrenViewModel = viewModel()
    val uiState by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        when {
            uiState.showLoading -> {
                LipsyLoading(
                    modifier = Modifier.fillMaxSize()
                )
            }
            uiState.showError -> {
                Text(
                    text = "Error al cargar productos",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                uiState.products?.let { products ->
                    ChildrenDataView(
                        products = products,
                        goToOptionScreen = { id ->
                            navController.navigateToDetails(category = CHILDREN, id = id)
                        }
                    )
                }
            }
        }
    }
}

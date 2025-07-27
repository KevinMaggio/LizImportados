package com.refactoringlife.lizimportadosv2.features.children.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import com.refactoringlife.lizimportadosv2.core.utils.isFalse
import com.refactoringlife.lizimportadosv2.features.children.presenter.viewmodel.ChildrenViewModel
import com.refactoringlife.lizimportadosv2.features.children.presenter.views.ChildrenDataView

@Composable
fun ChildrenScreen(
    navController: NavHostController
) {
    val viewModel: ChildrenViewModel = viewModel()
    val uiState by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        uiState.showLoading.isFalse {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
        uiState.showError.isFalse {
            Text(
                text = "Error al cargar productos",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        uiState.products?.let { products ->
            ChildrenDataView(
                products = products,
                goToOptionScreen = { id ->
                    navController.navigateToDetails(filter = CHILDREN, id = id)
                }
            )
        }
    }
}

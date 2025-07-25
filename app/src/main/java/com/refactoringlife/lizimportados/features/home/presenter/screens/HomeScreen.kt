package com.refactoringlife.lizimportados.features.home.presenter.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.navigator.DETAILS
import com.refactoringlife.lizimportados.features.home.presenter.viewmodel.HomeViewModel
import com.refactoringlife.lizimportados.features.home.presenter.views.HomeDataView

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.state.collectAsState()

    when {
        uiState.showLoading -> {
            // TODO: Mostrar loading
        }
        uiState.showError -> {
            // TODO: Mostrar error
        }
        else -> {
            uiState.config?.let { config ->
                HomeDataView(
                    modifier = modifier,
                    configData = config,
                    action = { filter, id -> navController.navigate("$DETAILS/$filter/$id") }
                )
            }
        }
    }
}
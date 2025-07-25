package com.refactoringlife.lizimportados.features.home.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.navigator.DETAILS
import com.refactoringlife.lizimportados.features.home.presenter.viewmodel.HomeViewModel
import com.refactoringlife.lizimportados.features.home.presenter.views.HomeDataView
import com.refactoringlife.lizimportados.ui.theme.TextBlue

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
) {
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.showLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center),
                    color = TextBlue
                )
            }
            uiState.showError -> {
                // TODO: Mostrar error
            }
            else -> {
                uiState.config?.let {
                    HomeDataView(
                        modifier = modifier,
                        state = uiState,
                        action = { filter, id -> navController.navigate("$DETAILS/$filter/$id") }
                    )
                }
            }
        }
    }
}
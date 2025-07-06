package com.refactoringlife.lizimportados.features.woman.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.navigator.navigateToDetails
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.woman.presenter.views.WomanDataView

@Composable
fun WomanScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()){
        WomanDataView(getProductsMock(),
            action = { id -> navController.navigateToDetails(filter = "woman", id = id) }
        )
    }
}
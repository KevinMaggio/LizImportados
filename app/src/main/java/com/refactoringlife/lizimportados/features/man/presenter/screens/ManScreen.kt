package com.refactoringlife.lizimportados.features.man.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.man.presenter.views.ManDataView

@Composable
fun ManScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        ManDataView(getProductsMock(),
            goToOptionScreen = { route, id -> navController.navigate("$route/$id") }
        )
    }
}

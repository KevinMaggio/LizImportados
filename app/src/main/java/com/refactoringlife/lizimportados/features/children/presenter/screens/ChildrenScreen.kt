package com.refactoringlife.lizimportados.features.children.presenter.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.children.presenter.views.ChildrenDataView

typealias route = String
typealias filter = String

@Composable
fun ChildrenScreen(navController: NavHostController) {
    ChildrenDataView(
        getProductsMock(),
        goToOptionScreen = {route, id -> navController.navigate("$route/$id") }
    )
}
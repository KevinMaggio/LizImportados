package com.refactoringlife.lizimportados.features.children.presenter.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.refactoringlife.lizimportados.core.navigator.CHILDREN
import com.refactoringlife.lizimportados.core.navigator.navigateToDetails
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.children.presenter.views.ChildrenDataView

@Composable
fun ChildrenScreen(navController: NavHostController) {
    ChildrenDataView(
        getProductsMock(),
        goToOptionScreen = {id -> navController.navigateToDetails(CHILDREN, id) }
    )
}
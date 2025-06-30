package com.refactoringlife.lizimportados.features.home.presenter.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.core.utils.getConfigMock
import com.refactoringlife.lizimportados.features.home.presenter.views.HomeDataView

typealias route = String
typealias filter = String

@Composable
fun HomeScreen(
    modifier: Modifier,
    goTo: (route, filter)-> Unit
) {
    HomeDataView(
        modifier = modifier,
        configData = getConfigMock(),
        goToOptionScreen = goTo
    )
}
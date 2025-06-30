package com.refactoringlife.lizimportados.features.woman.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.woman.presenter.views.WomanDataView

@Composable
fun WomanScreen() {
    Box(modifier = Modifier.fillMaxSize()){
        WomanDataView(getProductsMock())
    }
}
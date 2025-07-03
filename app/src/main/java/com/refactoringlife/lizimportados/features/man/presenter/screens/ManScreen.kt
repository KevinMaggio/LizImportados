package com.refactoringlife.lizimportados.features.man.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.man.presenter.views.ManDataView

@Composable
fun ManScreen () {
    Box(modifier = Modifier.fillMaxSize()) {
        ManDataView(getProductsMock())
    }
}

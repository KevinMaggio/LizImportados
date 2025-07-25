package com.refactoringlife.lizimportados.features.details.presenter.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.features.details.presenter.views.DetailsDataView

@Composable
fun DetailsScreen(filter: String?, id: String?) {
    Box(modifier = Modifier.fillMaxSize())
    {
        DetailsDataView(emptyList())
    }
}
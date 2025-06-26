package com.refactoringlife.lizimportados.features.home.presenter.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.features.home.composables.WeeklyOffersSection
import com.refactoringlife.lizimportados.features.utils.getProductsMock

@Composable
fun HomeDataView(
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.fillMaxSize()
        .padding(20.dp)) {
        Column {
            WeeklyOffersSection(
                "Ofertas semanales",
                getProductsMock()
            )
        }
    }
}
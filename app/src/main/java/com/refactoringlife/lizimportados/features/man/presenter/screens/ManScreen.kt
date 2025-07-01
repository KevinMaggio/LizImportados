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
        ManDataView(getProductsMock().map {
            ProductModel(
                id = it.id,
                title = it.title,
                subtitle = it.subtitle,
                url = "https://i.ibb.co/yccHNqLx/pngwing-com-1.png",
                price = it.price,
                oldPrice = it.oldPrice
            )
        }
        )
    }
}

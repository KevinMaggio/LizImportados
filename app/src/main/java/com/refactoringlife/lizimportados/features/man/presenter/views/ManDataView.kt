package com.refactoringlife.lizimportados.features.man.presenter.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyProduct
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.ui.theme.TextBlue

typealias id = String

@Composable
fun ManDataView (
    products: List<ProductModel>,
    action: (id) -> Unit
){
    Column (modifier = Modifier.fillMaxSize()
        .background(Color.White)
        .padding(start = 20.dp, top = 20.dp, bottom = 90.dp)){

        Text(text = "Seccion Hombre!",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            color = TextBlue,
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products){product ->
                LipsyProduct(
                    product= product,
                    isAvailable = true,
                    addCartProduct = {},
                    action = action
                )
            }
        }
    }
}
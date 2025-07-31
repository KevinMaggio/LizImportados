package com.refactoringlife.lizimportadosv2.features.cart.presenter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.features.cart.presenter.views.CartDataView
import com.refactoringlife.lizimportadosv2.ui.theme.CardBackGround

@Composable
fun CartScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBackGround)
    ) {
        CartDataView(
            ProductCartModel(
                subTotal = 45900,
                discount = 45900,
                products = listOf(
                    ProductCartModel.CartItemModel(
                        name = "Chaqueta de Jeans",
                        image = "https://firebasestorage.googleapis.com/v0/b/liz-importados.firebasestorage.app/o/productos%2F4e38cdf8-427b-4af2-9a6b-43c513694802.jpg?alt=media&token=ee03fcc2-a7d8-444b-9e3f-2fc91bc26e97",
                        season = "XL",
                        available = true,
                        price = 45900
                    ),
                    ProductCartModel.CartItemModel(
                        name = "Chaqueta de Jeans",
                        image = "https://firebasestorage.googleapis.com/v0/b/liz-importados.firebasestorage.app/o/productos%2F4e38cdf8-427b-4af2-9a6b-43c513694802.jpg?alt=media&token=ee03fcc2-a7d8-444b-9e3f-2fc91bc26e97",
                        season = "XL",
                        available = true,
                        price = 45900
                    ),
                    ProductCartModel.CartItemModel(
                        name = "Chaqueta de Jeans",
                        image = "https://firebasestorage.googleapis.com/v0/b/liz-importados.firebasestorage.app/o/productos%2F4e38cdf8-427b-4af2-9a6b-43c513694802.jpg?alt=media&token=ee03fcc2-a7d8-444b-9e3f-2fc91bc26e97",
                        season = "XL",
                        available = true,
                        price = 45900
                    ),
                    ProductCartModel.CartItemModel(
                        name = "Chaqueta de Jeans",
                        image = "https://firebasestorage.googleapis.com/v0/b/liz-importados.firebasestorage.app/o/productos%2F4e38cdf8-427b-4af2-9a6b-43c513694802.jpg?alt=media&token=ee03fcc2-a7d8-444b-9e3f-2fc91bc26e97",
                        season = "XL",
                        available = false,
                        price = 45900
                    ),
                    ProductCartModel.CartItemModel(
                        name = "Chaqueta de Jeans",
                        image = "https://firebasestorage.googleapis.com/v0/b/liz-importados.firebasestorage.app/o/productos%2F4e38cdf8-427b-4af2-9a6b-43c513694802.jpg?alt=media&token=ee03fcc2-a7d8-444b-9e3f-2fc91bc26e97",
                        season = "XL",
                        available = false,
                        price = 45900
                    )
                ),
                total = 45900
            )
        )
    }
}
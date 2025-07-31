package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyMoreItems
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyWhatsAppButton
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary

@Composable
fun CartDataView(
    product: ProductCartModel
) {
    val availableProducts = product.products.filter { it.available }

    val sealedProducts = product.products.filter { !it.available }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        item {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Carrito de compras",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                color = TextBlue
            )
            Spacer(Modifier.height(20.dp))
        }

        item {
            availableProducts.forEach {
                CartItem(cartItemModel = it)
                LipsyDivider()
            }
        }

        item {
            TotalSection(
                subtotal = product.subTotal,
                discount = product.discount,
                total = product.total
            )
        }

        item {
            Spacer(Modifier.height(20.dp))
            LipsyWhatsAppButton(
                action = {}
            )
        }

        item {
            DividerInner()
        }

        item {
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Carrito de compras",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                color = TextPrimary
            )

            Spacer(Modifier.height(20.dp))
        }

        item {
            sealedProducts.forEach {
                CartItem(cartItemModel = it)
                Spacer(Modifier.height(60.dp))
            }
        }

        item {
            Spacer(Modifier.height(20.dp))
            LipsyMoreItems(
                action = {}
            )
        }
    }
}

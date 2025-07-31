package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
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
                fontSize = 16.sp,
                color = TextBlue
            )
            Spacer(Modifier.height(20.dp))
        }

        item {
            availableProducts.forEach {
                LipsyDivider()
                CartItem(cartItemModel = it)
            }
        }

        item {
            Spacer(Modifier.height(20.dp))

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

            Spacer(Modifier.height(20.dp))

        }

        item {
            Spacer(Modifier.height(20.dp))

            Text(
                text = "Productos no disponibles",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 16.sp,
                color = TextBlue
            )

            Spacer(Modifier.height(20.dp))
        }

        item {
            sealedProducts.forEach {
                LipsyDivider()
                CartItem(cartItemModel = it)
            }
        }

        item {
            Spacer(Modifier.height(20.dp))

            HorizontalDivider(color = Color.Black)

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Estos productos fueron vendidos mientras armabas el carrito, lo sentimos",
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 12.sp,
                color = TextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))
        }

        item {
            Spacer(Modifier.height(20.dp))

            LipsyMoreItems(
                action = {}
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary


@Composable
fun CartItem(
    cartItemModel: ProductCartModel.CartItemModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        LipsyAsyncImage(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(70.dp)
                .width(50.dp),
            url = cartItemModel.image
        )

        Column {
            Text(
                text = cartItemModel.name.orEmpty(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                color = TextPrimary
            )

            Text(
                text = "Talla: " + cartItemModel.season.orEmpty(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 12.sp,
                color = TextPrimary
            )

            Spacer(Modifier.height(10.dp))

            if (cartItemModel.available) {
                Text(
                    text = "Eliminar",
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 10.sp,
                    color = TextBlue
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = "$" + cartItemModel.price.toString(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 12.sp,
                color = TextPrimary
            )
        }
    }
}
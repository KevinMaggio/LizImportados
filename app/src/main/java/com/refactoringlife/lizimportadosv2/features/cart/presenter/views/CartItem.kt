package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.ui.theme.CardBackGround
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary


@Composable
fun CartItem(
    cartItemModel: ProductCartModel.CartItemModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 16.dp)
                .height(70.dp)
                .width(50.dp)
                .background(CardBackGround, RoundedCornerShape(8.dp))
        ) {
            if (cartItemModel.image.isNullOrEmpty()) {
                Image(
                    painter = painterResource(R.drawable.icon_default_clothes),
                    contentDescription = "no image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                AsyncImage(
                    model = cartItemModel.image,
                    contentDescription = "product image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
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

        Text(
            text = "$" + cartItemModel.price.toString(),
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 12.sp,
            color = TextPrimary,
            modifier = Modifier.padding(end = 20.dp)
        )
    }
}
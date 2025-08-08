package com.refactoringlife.lizimportadosv2.features.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboResponse
import com.refactoringlife.lizimportadosv2.ui.theme.CardBackGround
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary
import com.refactoringlife.lizimportadosv2.ui.theme.TextSecondary

@Composable
fun ComboItem(
    combo: ComboResponse,
    lastItem: Boolean,
    onAddToCart: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackGround),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LipsyAsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                combo.firstProduct.image
            )

            Spacer(Modifier.width(10.dp))

            Image(
                painter = painterResource(R.drawable.icon_plus),
                contentDescription = "No description",
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(10.dp))

            LipsyAsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .height(120.dp),
                combo.secondProduct.image
            )

            Spacer(
                modifier = Modifier
                    .weight(0.5f)
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$" + combo.oldPrice.toString(),
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    textDecoration = TextDecoration.LineThrough,
                    color = TextBlue
                )
                Text(
                    text = "$" + combo.price.toString(),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    color = TextPrimary
                )
            }
        }
        Row {
            Text(
                text = "${combo.firstProduct.brand} + ${combo.secondProduct.brand}",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = TextPrimary
            )
        }
        Row {
            Text(
                text = "${combo.firstProduct.description} + ${combo.secondProduct.description}",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                color = TextSecondary
            )
        }
        
        Spacer(Modifier.height(8.dp))
        
        // Botón Agregar Combo (mismo diseño que Details)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onAddToCart(combo.id) }
            ) {
                Text(
                    text = "Agregar a carrito",
                    fontSize = 14.sp,
                    color = TextPrimary,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                )

                Image(
                    painter = painterResource(R.drawable.icon_more),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(20.dp)
                )
            }
        }
    }

    if (!lastItem) {
        LipsyDivider()
    } else {
        Spacer(Modifier.height(10.dp))
    }
}

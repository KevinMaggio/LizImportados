package com.refactoringlife.lizimportadosv2.core.composablesLipsy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.utils.capitalizeWords
import com.refactoringlife.lizimportadosv2.core.utils.isValid
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary
import com.refactoringlife.lizimportadosv2.ui.theme.TextSecondary

typealias id = String

@Composable
fun LipsyProduct(
    product: ProductModel,
    isAvailable: Boolean = false,
    addCartProduct: (String) -> Unit,
    action: (id) -> Unit,
    isLarge: Boolean = false
) {
    Column(modifier = Modifier
        .padding(if (isLarge) 5.dp else 20.dp)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            action.invoke(product.id)
        }) {
        LipsyCardImage(
            product.images[0], 
            modifier = Modifier
                .width(if (isLarge) 170.dp else 100.dp)
                .height(if (isLarge) 220.dp else 150.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        product.name?.isValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = if (isLarge) 14.sp else 12.sp,
                lineHeight = 1.sp,
                color = TextSecondary
            )
        }

        product.brand?.isValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = if (isLarge) 12.sp else 10.sp,
                lineHeight = 6.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        if(product.isOffer) {
            Text(
                text ="$" +  product.price.orEmpty().capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = if (isLarge) 12.sp else 10.sp,
                lineHeight = 1.sp,
                textDecoration = TextDecoration.LineThrough,
                color = TextBlue
            )

            Text(
                text = "$" + product.offersPrice.orEmpty().capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = if (isLarge) 16.sp else 14.sp,
                lineHeight = 1.sp,
                color = TextPrimary
            )
        } else {
            product.price?.isValid {
                Text(
                    text = "$" + it.capitalizeWords(),
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = if (isLarge) 16.sp else 14.sp,
                    lineHeight = 1.sp,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (isAvailable) {
            Text(
                text = stringResource(R.string.add_cart),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = if (isLarge) 12.sp else 10.sp,
                lineHeight = 1.sp,
                color = TextBlue,
                modifier = Modifier.clickable {
                    addCartProduct(product.id)
                }
            )
        }
    }
}
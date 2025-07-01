package com.refactoringlife.lizimportados.core.composablesLipsy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.core.utils.capitalizeWords
import com.refactoringlife.lizimportados.core.utils.onValid
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.ui.theme.TextBlue
import com.refactoringlife.lizimportados.ui.theme.TextPrimary
import com.refactoringlife.lizimportados.ui.theme.TextSecondary

@Composable
fun LipsyProduct(
    product: ProductModel,
    isAvailable: Boolean = false,
    addCartProduct: (String) -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {
        LipsyCardImage(product.images[0])

        Spacer(modifier = Modifier.height(10.dp))

        product.title?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 12.sp,
                lineHeight = 1.sp,
                color = TextSecondary
            )
        }

        product.subtitle?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                lineHeight = 6.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        product.oldPrice?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                lineHeight = 1.sp,
                textDecoration = TextDecoration.LineThrough,
                color = TextBlue
            )
        }

        product.price?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                lineHeight = 1.sp,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (isAvailable) {
            Text(
                text = stringResource(R.string.add_cart),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                lineHeight = 1.sp,
                color = TextBlue,
                modifier = Modifier.clickable {
                    addCartProduct(product.id)
                }
            )
        }
    }
}
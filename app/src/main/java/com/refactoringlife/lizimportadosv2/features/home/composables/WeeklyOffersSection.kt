package com.refactoringlife.lizimportadosv2.features.home.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyProduct
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.core.utils.capitalizeWords
import com.refactoringlife.lizimportadosv2.core.utils.isValid
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue

typealias id = String

@Composable
fun WeeklyOffersSection (
    title : String?,
    products: List<ProductModel>,
    action: (id) -> Unit
){
    title?.isValid {
        Text(
            text = it.capitalizeWords(),
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            fontSize = 14.sp,
            color = TextBlue
        )
    }

    LazyRow(modifier = Modifier.fillMaxWidth()){
        items(products){product ->
            LipsyProduct(
                product=product,
                addCartProduct = {},
                action = { action.invoke(product.id) }
            )
        }
    }
}

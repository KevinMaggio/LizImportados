package com.refactoringlife.lizimportados.features.home.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.features.composablesLipsy.LipsyProduct
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.utils.capitalizeWords
import com.refactoringlife.lizimportados.features.utils.onValid
import com.refactoringlife.lizimportados.ui.theme.TextBlue

@Composable
fun WeeklyOffersSection (
    title : String?,
    products: List<ProductModel>
){
    title?.onValid {
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
                url = product.url,
                title = product.title,
                subtitle = product.subtitle,
                oldPrice = product.oldPrice,
                price = product.price
            )
        }
    }
}

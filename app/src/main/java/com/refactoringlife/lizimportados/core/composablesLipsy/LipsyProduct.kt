package com.refactoringlife.lizimportados.core.composablesLipsy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.core.utils.capitalizeWords
import com.refactoringlife.lizimportados.core.utils.onValid
import com.refactoringlife.lizimportados.ui.theme.TextBlue
import com.refactoringlife.lizimportados.ui.theme.TextPrimary
import com.refactoringlife.lizimportados.ui.theme.TextSecondary

@Composable
fun LipsyProduct(
    url: String?,
    title: String?,
    subtitle: String?,
    oldPrice: String?,
    price: String?
) {
    Column(modifier = Modifier.padding(20.dp)) {
        LipsyCardImage(url)

        Spacer(modifier = Modifier.height(10.dp))

        title?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 12.sp,
                lineHeight = 1.sp,
                color = TextSecondary
            )
        }

        subtitle?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                lineHeight = 6.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        oldPrice?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                lineHeight = 1.sp,
                textDecoration = TextDecoration.LineThrough,
                color = TextBlue
            )
        }

        price?.onValid {
            Text(
                text = it.capitalizeWords(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                lineHeight = 1.sp,
                color = TextPrimary
            )
        }
    }
}
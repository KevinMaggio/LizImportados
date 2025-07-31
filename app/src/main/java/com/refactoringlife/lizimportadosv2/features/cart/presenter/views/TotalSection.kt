package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary

@Composable
fun TotalSection(
    subtotal: Int,
    discount: Int,
    total: Int
) {
    Column {
        HorizontalDivider(color = Color.Black)

        Spacer(Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Subtotal",
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                color = TextPrimary
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "$$subtotal",
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                color = TextPrimary
            )
        }

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Descuento",
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                color = TextPrimary
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "$${discount}",
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 10.sp,
                color = TextPrimary
            )
        }

        Row(Modifier.fillMaxWidth()) {
            Text(
                text = "Total",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                color = TextPrimary
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "$${total}",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 14.sp,
                color = TextPrimary
            )
        }

        Spacer(Modifier.height(10.dp))

        HorizontalDivider(color = Color.Black)
    }

}
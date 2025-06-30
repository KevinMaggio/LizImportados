package com.refactoringlife.lizimportados.features.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportados.core.utils.getComboMock
import com.refactoringlife.lizimportados.features.home.data.model.CombosModel
import com.refactoringlife.lizimportados.ui.theme.CardBackGround

@Composable
fun ComboItem(
    combo: CombosModel.ComboModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
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
                    .height(100.dp),
                combo.firstProduct.image
            )

            Image(
                painter = painterResource(R.drawable.icon_plus),
                contentDescription = "No description",
                modifier = Modifier.size(15.dp)
            )

            LipsyAsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                combo.secondProduct.image)

            Spacer(modifier = Modifier
                    .weight(0.5f))

            Column (
                modifier = Modifier
                    .weight(1f)){
                Text(
                    text = combo.oldPrice.toString(),
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
                Text(
                    text = combo.price.toString(),
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            }
        }
        Row {
            Text(
                text = "${combo.firstProduct.brand} + ${combo.secondProduct.brand}",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        }
        Row {
            Text(
                text = "${combo.firstProduct.description} + ${combo.secondProduct.description}",
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }

    LipsyDivider()
}

@Preview
@Composable
fun example() {
    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .shadow(elevation = 8.dp)
                .background(CardBackGround)
        ) {
            LazyColumn {
                items(getComboMock().combos) {
                    ComboItem(
                        combo = it
                    )
                }
            }
        }
    }
}
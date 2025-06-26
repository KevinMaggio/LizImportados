package com.refactoringlife.lizimportados.features.home.presenter.views

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.features.composablesLipsy.LipsyCircleButton
import com.refactoringlife.lizimportados.features.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportados.features.home.composables.WeeklyOffersSection
import com.refactoringlife.lizimportados.features.utils.getProductsMock
import com.refactoringlife.lizimportados.ui.theme.CardBackGround
import com.refactoringlife.lizimportados.ui.theme.ColorGrayLipsy

@Composable
fun HomeDataView(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp)
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.liz_importados),
                contentDescription = "",
                contentScale = ContentScale.Inside
            )

            Spacer(Modifier.height(20.dp))

            Image(
                modifier = Modifier.padding(start = 50.dp, end = 70.dp),
                painter = painterResource(R.drawable.title_logo),
                contentDescription = ""
            )

            Spacer(Modifier.height(20.dp))

            WeeklyOffersSection(
                "Ofertas semanales",
                getProductsMock()
            )

            LipsyDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    LipsyCircleButton(
                        modifier = Modifier.size(70.dp),
                        action = {},
                        text = "invierno",
                        background = CardBackGround
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    LipsyCircleButton(
                        modifier = Modifier.size(70.dp),
                        action = {},
                        text = "invierno",
                        background = CardBackGround
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    LipsyCircleButton(
                        modifier = Modifier.size(70.dp),
                        action = {},
                        text = "invierno",
                        background = CardBackGround
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(10.dp)
                ) {
                    LipsyCircleButton(
                        modifier = Modifier.size(70.dp),
                        action = {},
                        text = "invierno",
                        background = CardBackGround
                    )
                }
            }
        }
    }
}
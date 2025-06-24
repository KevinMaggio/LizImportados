package com.refactoringlife.lizimportados.features.bottomBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.ui.theme.CircleFilterColor

@Composable
fun LipsyBottomBar(
    goTo: (DestinationsBottomBar) -> Unit
) {
    var filtersIsActivated by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .drawBehind {
                    val strokeWidth = 1.dp.toPx()
                    drawLine(
                        color = CircleFilterColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = strokeWidth
                    )
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { goTo(DestinationsBottomBar.HOME) }
                    .padding(vertical = 15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_home),
                    contentDescription = "Home",
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    text = "Home",
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { goTo(DestinationsBottomBar.CART) }
                    .padding(vertical = 15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_cart),
                    contentDescription = "Cart",
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    text = "Cart",
                    fontSize = 10.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_center),
                contentDescription = "Center",
                modifier = Modifier
                    .size(100.dp)
                    .offset(y = (-20).dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        filtersIsActivated = !filtersIsActivated
                        goTo(DestinationsBottomBar.CENTER)
                    }
            )
        }

//Floating buttons

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircleAnimateButton(
                modifier = Modifier
                    .padding(start = 50.dp)
                    .size(80.dp)
                    .align(Alignment.Bottom)
                    .padding(10.dp),
                isVisible = filtersIsActivated,
                text = stringResource(R.string.bottom_bar_man),
                goTo = { goTo.invoke(DestinationsBottomBar.MAN) },
                fadeIn = 300,
                fadeOut = 300
            )

            Spacer(modifier = Modifier.weight(1f))

            CircleAnimateButton(
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .size(80.dp)
                    .align(Alignment.Top)
                    .padding(10.dp),
                isVisible = filtersIsActivated,
                text = stringResource(R.string.bottom_bar_children),
                goTo = { goTo.invoke(DestinationsBottomBar.CHILDREN) },
                fadeIn = 900,
                fadeOut = 300
            )

            Spacer(modifier = Modifier.weight(1f))

            CircleAnimateButton(
                modifier = Modifier
                    .padding(end = 50.dp)
                    .size(80.dp)
                    .align(Alignment.Top)
                    .padding(10.dp),
                isVisible = filtersIsActivated,
                text = stringResource(R.string.bottom_bar_woman),
                goTo = { goTo.invoke(DestinationsBottomBar.WOMAN) },
                fadeIn = 1300,
                fadeOut = 300
            )
        }
    }
}
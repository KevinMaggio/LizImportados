package com.refactoringlife.lizimportados.features.composablesLipsy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import kotlinx.coroutines.delay

enum class BottomBarItem { HOME, CENTER, CART }

@Composable
fun LipsyBottomBar(
    selected: BottomBarItem,
    goToHome: () -> Unit,
    goToContent: () -> Unit,
    goToCart: () -> Unit
) {
    var filtersIsActivated by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Barra inferior
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { goToHome() }
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
                    .clickable { goToCart() }
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
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = filtersIsActivated,
                enter = fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(1200))
            ) {
                LipsyCircleButton(
                    text = "Hombre",
                    modifier = Modifier.offset(x = (-120).dp, y = (-90).dp)
                )
            }
            AnimatedVisibility(
                visible = filtersIsActivated,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(800))
            ) {
                LipsyCircleButton(
                    text = "Niños",
                    modifier = Modifier.offset(y = (-140).dp)
                )
            }
            AnimatedVisibility(
                visible = filtersIsActivated,
                enter = fadeIn(animationSpec = tween(600)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                LipsyCircleButton(
                    text = "Mujer",
                    modifier = Modifier.offset(x = 120.dp, y = (-90).dp)
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
                    .clickable {
                        filtersIsActivated = !filtersIsActivated
                        goToContent()
                    }
            )
        }
    }
}
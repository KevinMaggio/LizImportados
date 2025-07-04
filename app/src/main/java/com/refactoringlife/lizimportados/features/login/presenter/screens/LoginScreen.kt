package com.refactoringlife.lizimportados.features.login.presenter.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.refactoringlife.lizimportados.R

@Preview
@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(R.drawable.liz_importados),
            contentDescription = "",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopCenter)
        )

        Image(
            painter = painterResource(R.drawable.background_login),
            contentDescription = "",
            modifier = Modifier
                .height(600.dp)
                .align(Alignment.BottomStart)
        )

        Image(
            painter = painterResource(R.drawable.icon_login_butterfly),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 70.dp, end = 40.dp)
                .size(40.dp)
                .align(Alignment.TopEnd)
        )

        Image(
            painter = painterResource(R.drawable.icon_login_google),
            contentDescription = "",
            modifier = Modifier
                .padding(bottom = 150.dp)
                .size(120.dp)
                .align(Alignment.BottomCenter)
        )

        LottieAnimationButterfly(modifier = Modifier.align(Alignment.BottomCenter)
            .padding(top = 70.dp, start = 50.dp, bottom = 140.dp))
    }
}

@Composable
fun LottieAnimationButterfly(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lootie_butterfly.json"))
    val progress by animateLottieCompositionAsState(composition,
        iterations = LottieConstants.IterateForever)

    LottieAnimation(
        composition,
        progress,
        modifier = modifier
            .size(60.dp)
    )
}
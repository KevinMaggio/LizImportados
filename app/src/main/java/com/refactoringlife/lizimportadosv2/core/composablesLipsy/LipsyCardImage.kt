package com.refactoringlife.lizimportadosv2.core.composablesLipsy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.utils.shimmerEffect
import com.refactoringlife.lizimportadosv2.ui.theme.CardBackGround

@Composable
fun LipsyCardImage(url: String?, modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        // Imagen por debajo
        if (url.isNullOrEmpty()) {
            isLoading = false
            Image(
                painter = painterResource(R.drawable.icon_default_clothes),
                contentDescription = "no image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .background(CardBackGround)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            AsyncImage(
                model = url,
                contentDescription = "generic image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .background(CardBackGround)
                    .clip(RoundedCornerShape(8.dp)),
                onState = { state ->
                    isLoading = state is AsyncImagePainter.State.Loading
                }
            )
        }

        // Shimmer por encima solo cuando está cargando
        if (isLoading) {
            Box(
                modifier = modifier
                    .shimmerEffect()
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

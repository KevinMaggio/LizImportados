package com.refactoringlife.lizimportados.features.composablesLipsy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.features.utils.shimmerEffect
import com.refactoringlife.lizimportados.ui.theme.CardBackGround

@Composable
fun LipsyCardImage(url: String?) {
    val context = LocalContext.current

    url?.let {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build()
        )

        val state = painter.state

        Surface(
            modifier = Modifier
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
        ) {
            when (state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardBackGround)
                            .shimmerEffect()
                    )
                }

                is AsyncImagePainter.State.Error -> {
                    Image(
                        painter = painterResource(R.drawable.icon_default_clothes),
                        contentDescription = "Descripción",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .height(150.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                    )
                }

                else -> {
                    Image(
                        painter = painter,
                        contentDescription = "Descripción",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .height(150.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                    )
                }
            }
        }
    } ?: Image(
        painter = painterResource(R.drawable.icon_default_clothes),
        contentDescription = "Descripción",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(100.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(12.dp)
            )
    )
}

package com.refactoringlife.lizimportadosv2.core.composablesLipsy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@Composable
fun LipsyZoomableImage(
    images: List<String>,
    initialImageIndex: Int = 0,
    onDismiss: () -> Unit = {}
) {
    var currentImageIndex by remember { mutableStateOf(initialImageIndex) }
    val imagePagerState = rememberPagerState(
        initialPage = initialImageIndex,
        pageCount = { images.size }
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Header con contador de imágenes (no bloquea gestos al no ocupar todo el ancho)
                Text(
                    text = "${currentImageIndex + 1} / ${images.size}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )

                // Botón de cerrar
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_more),
                        contentDescription = "Cerrar",
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                // Pager de imágenes con zoom
                HorizontalPager(
                    state = imagePagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    currentImageIndex = page
                    ZoomableImage(
                        imageUrl = images[page],
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp)
                    )
                }

                // Gestos de swipe horizontal en TODA la pantalla (también fuera de la imagen)
                val scope = rememberCoroutineScope()
                val density = LocalDensity.current
                var accumulatedDragX by remember { mutableStateOf(0f) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(imagePagerState.currentPage) {
                            detectHorizontalDragGestures(
                                onDragStart = { accumulatedDragX = 0f },
                                onHorizontalDrag = { _, dragAmount ->
                                    accumulatedDragX += dragAmount
                                },
                                onDragEnd = {
                                    val threshold = with(density) { 56.dp.toPx() }
                                    val drag = accumulatedDragX
                                    accumulatedDragX = 0f
                                    if (drag <= -threshold && currentImageIndex < images.lastIndex) {
                                        scope.launch { imagePagerState.animateScrollToPage(currentImageIndex + 1) }
                                    } else if (drag >= threshold && currentImageIndex > 0) {
                                        scope.launch { imagePagerState.animateScrollToPage(currentImageIndex - 1) }
                                    }
                                }
                            )
                        }
                )
            }
        }
    }
}

@Composable
private fun ZoomableImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(modifier = modifier.clip(RectangleShape)) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen del producto",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f..3f)

                        val newOffsetX = offsetX + pan.x
                        val newOffsetY = offsetY + pan.y

                        // Limitar el desplazamiento para evitar que la imagen se salga demasiado
                        val maxOffsetX = (size.width * (scale - 1f)) / 2f
                        val maxOffsetY = (size.height * (scale - 1f)) / 2f

                        offsetX = newOffsetX.coerceIn(-maxOffsetX, maxOffsetX)
                        offsetY = newOffsetY.coerceIn(-maxOffsetY, maxOffsetY)
                    }
                }
        )
    }
} 
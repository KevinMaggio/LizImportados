package com.refactoringlife.lizimportadosv2.core.composablesLipsy

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
                // Header con contador de imágenes
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = "${currentImageIndex + 1} / ${images.size}",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        fontSize = 16.sp
                    )
                }

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
                            .padding(horizontal = 20.dp, vertical = 60.dp)
                    )
                }
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

    Box(
        modifier = modifier
            .clip(RectangleShape)
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
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Imagen del producto",
            modifier = Modifier.fillMaxSize()
        )
    }
} 
package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun DividerInner (){
    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.Gray, shape = RoundedCornerShape(12.dp))
            .drawBehind {
                // Simulación de sombra interior
                val shadowColor = Color.Black.copy(alpha = 0.4f)
                val blurRadius = 10.dp.toPx()

                drawIntoCanvas { canvas ->
                    val paint = Paint().asFrameworkPaint().apply {
                        isAntiAlias = true
                        color = android.graphics.Color.TRANSPARENT
                        setShadowLayer(blurRadius, 0f, 0f, shadowColor.toArgb())
                    }

                    val left = 0f
                    val top = 0f
                    val right = size.width
                    val bottom = size.height

                    canvas.nativeCanvas.drawRect(
                        left + blurRadius,
                        top + blurRadius,
                        right - blurRadius,
                        bottom - blurRadius,
                        paint
                    )
                }
            }
    )
}
package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.ui.theme.TextSecondary

@Composable
fun DividerInner (){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(15.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.2f),
                        TextSecondary
                    )
                )
            )
    )
}
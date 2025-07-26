package com.refactoringlife.lizimportadosv2.core.composablesLipsy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.utils.EMPTY
import com.refactoringlife.lizimportadosv2.ui.theme.CircleFilterColor

@Composable
fun LipsyCircleButton(
    modifier: Modifier = Modifier,
    action: () -> Unit,
    text: String = EMPTY,
    imageUrl: String? = null,
    background: Color = CircleFilterColor,
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .shadow(2.dp, shape = CircleShape, clip = true)
            .clip(CircleShape)
            .background(background)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                action.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = text,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            Text(
                text = text,
                maxLines = 2,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}

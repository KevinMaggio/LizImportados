package com.refactoringlife.lizimportados.features.composablesLipsy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.ui.theme.CircleFilterColor

@Composable
fun LipsyCircleButton(
    modifier: Modifier = Modifier,
    goTo: () -> Unit,
    text: String = ""
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .shadow(2.dp, shape = CircleShape, clip = true)
            .clip(CircleShape)
            .background(CircleFilterColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                goTo.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
    }
}

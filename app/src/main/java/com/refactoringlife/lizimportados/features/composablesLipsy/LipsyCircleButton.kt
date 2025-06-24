package com.refactoringlife.lizimportados.features.composablesLipsy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    text: String = ""
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .shadow(8.dp, shape = CircleShape, clip = false)
            .clip(CircleShape)
            .background(CircleFilterColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        )
    }
}

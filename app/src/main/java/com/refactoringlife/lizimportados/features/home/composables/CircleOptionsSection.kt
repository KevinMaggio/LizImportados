package com.refactoringlife.lizimportados.features.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyCircleButton
import com.refactoringlife.lizimportados.ui.theme.CardBackGround

@Composable
fun CircleOptionsSection(
    options: List<String>,
    action: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
    ) {
        options.forEach { text ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clickable(
                        onClick = { action.invoke(text) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                LipsyCircleButton(
                    modifier = Modifier.size(70.dp),
                    action = {},
                    text = text,
                    background = CardBackGround
                )
            }
        }
    }
}
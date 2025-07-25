package com.refactoringlife.lizimportados.features.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyCircleButton
import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.ui.theme.CardBackGround

@Composable
fun CircleOptionsSection(
    options: List<ConfigResponse.Option>,
    action: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
    ) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                LipsyCircleButton(
                    modifier = Modifier.size(70.dp),
                    action = { action.invoke(option.name) },
                    text = option.name,
                    imageUrl = option.image,
                    background = CardBackGround
                )
            }
        }
    }
}
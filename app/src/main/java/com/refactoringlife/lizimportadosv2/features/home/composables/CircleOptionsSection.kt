package com.refactoringlife.lizimportadosv2.features.home.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyOptionsButton
import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse

@Composable
fun CircleOptionsSection(
    options: List<ConfigResponse.Option>,
    action: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            LipsyOptionsButton(
                modifier = Modifier.width(70.dp),
                action = { action.invoke(option.name) },
                text = option.name,
                imageUrl = option.image,
                background = Color.White
            )
        }
    }
}
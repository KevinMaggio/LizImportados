package com.refactoringlife.lizimportados.features.bottomBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.refactoringlife.lizimportados.features.composablesLipsy.LipsyCircleButton

@Composable
fun CircleAnimateButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    text: String = "",
    goTo: () -> Unit,
    fadeIn: Int,
    fadeOut: Int
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(fadeIn)),
        exit = fadeOut(animationSpec = tween(fadeOut))
    ) {
        Box(
            modifier = modifier
        ) {
            LipsyCircleButton(
                text = text,
                action = goTo
            )
        }
    }
}
package com.refactoringlife.lizimportados.features.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.core.utils.capitalizeWords
import com.refactoringlife.lizimportados.ui.theme.CardBackGround
import com.refactoringlife.lizimportados.ui.theme.TextBlue

@Composable
fun ComboSection (

){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
    ) {
        Text(
            text = it.capitalizeWords(),
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            fontSize = 14.sp,
            color = TextBlue
        )

        Card(modifier = Modifier.shadow(elevation = 8.dp)
            .background(CardBackGround)) {
            LazyColumn {
                items(){

                }
            }
        }
    }
}
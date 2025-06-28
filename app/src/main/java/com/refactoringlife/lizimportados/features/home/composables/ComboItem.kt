package com.refactoringlife.lizimportados.features.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyProduct
import java.sql.RowId

@Composable
fun ComboItem(
    firstProduct : String
) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(20.dp)) {
        Row {
            LipsyAsyncImage()

            Image(
                painter = painterResource(),
                contentDescription = ""
            )

            LipsyAsyncImage()

            Column {
                Text()
                Text()
            }
        }
        Row {

        }
        Row {

        }
    }
}
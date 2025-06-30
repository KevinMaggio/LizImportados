package com.refactoringlife.lizimportados.features.home.presenter.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.R
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.core.utils.getComboMock
import com.refactoringlife.lizimportados.features.home.composables.WeeklyOffersSection
import com.refactoringlife.lizimportados.core.utils.getProductsMock
import com.refactoringlife.lizimportados.features.home.composables.CircleOptionsSection
import com.refactoringlife.lizimportados.features.home.composables.ComboSection

@Composable
fun HomeDataView(
    modifier: Modifier = Modifier,
    configData: ConfigResponse,
    goToOptionScreen: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, top = 40.dp)
    ) {
        // Header con logo y título
        item {
            Column {
                Image(
                    painter = painterResource(R.drawable.liz_importados),
                    contentDescription = "",
                    contentScale = ContentScale.Inside
                )

                Spacer(Modifier.height(20.dp))

                Image(
                    modifier = Modifier.padding(start = 50.dp, end = 70.dp),
                    painter = painterResource(R.drawable.title_logo),
                    contentDescription = ""
                )

                Spacer(Modifier.height(20.dp))
            }
        }

        // Weekly Offers Section
        if (configData.weeklyOffers) {
            item {
                WeeklyOffersSection(
                    title = stringResource(R.string.weekly_offers),
                    products = getProductsMock()
                )
                LipsyDivider()
            }
        }

        // Circle Options Section
        if (configData.circleOptions.isNotEmpty()) {
            item {
                CircleOptionsSection(
                    options = configData.circleOptions,
                    action = { filter ->
                        goToOptionScreen(filter)
                    }
                )
                LipsyDivider()
            }
        }

        // Combo Section - Se expande en su totalidad
        item {
            ComboSection(getComboMock())
        }
    }
}
package com.refactoringlife.lizimportadosv2.features.home.presenter.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportadosv2.core.utils.isTrue
import com.refactoringlife.lizimportadosv2.features.home.composables.WeeklyOffersSection
import com.refactoringlife.lizimportadosv2.features.home.composables.CircleOptionsSection
import com.refactoringlife.lizimportadosv2.features.home.composables.ComboSection
import com.refactoringlife.lizimportadosv2.features.home.domain.state.HomeUiState
import com.refactoringlife.lizimportadosv2.ui.theme.ColorWhiteLipsy

typealias category = String
typealias id = String

@Composable
fun HomeDataView(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    action: (category, id) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ColorWhiteLipsy)
            .padding(start = 20.dp, end = 20.dp, top = 40.dp)
    ) {
        item {
            Column {
                Image(
                    painter = painterResource(R.drawable.liz_importados),
                    contentDescription = "",
                    contentScale = ContentScale.Inside
                )

                Spacer(Modifier.height(20.dp))
            }
        }

        state.config?.isOffers?.isTrue{
            item {
                WeeklyOffersSection(
                    title = stringResource(R.string.weekly_offers),
                    products = state.offersProducts,
                    action = { action.invoke("ofertas", it) }
                )
                LipsyDivider()
            }
        }

        state.config?.circleOptions?.let{
            item {
                CircleOptionsSection(
                    options = state.config.circleOptions,
                    action = { category ->
                        action(category, "")
                    }
                )
                LipsyDivider()
            }
        }

        state.config?.hasCombo?.isTrue { combos ->
            item {
                ComboSection(state.combosModel)
            }
        }

        item {
            Spacer(Modifier.height(160.dp))
        }
    }
}
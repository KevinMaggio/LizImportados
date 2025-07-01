package com.refactoringlife.lizimportados.features.details.presenter.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportados.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow
import com.refactoringlife.lizimportados.ui.theme.CardBackGround
import com.refactoringlife.lizimportados.ui.theme.TextBlue

@Composable
fun DetailsDataView(products: List<ProductModel>) {
    val productPagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { products.size }
    )
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).
        padding(top = 30.dp, bottom = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VerticalPager(
            state = productPagerState,
            modifier = Modifier.fillMaxSize()
        ) { productPage ->
            val product = products[productPage]
            val imagePagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f,
                pageCount = { product.images.size }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = imagePagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                ) { imagePage ->
                    Surface(
                        modifier = Modifier
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
                            .background(Color.Red)
                    ) {
                        LipsyAsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            url = product.images[imagePage]
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(product.images.size) { index ->
                        val color = if (imagePagerState.currentPage == index) TextBlue else Color.LightGray
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(color)
                                .padding(2.dp)
                        )
                        if (index < product.images.size - 1) Spacer(modifier = Modifier.width(6.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = product.title ?: "", style = MaterialTheme.typography.titleLarge)
                Text(text = product.subtitle ?: "", style = MaterialTheme.typography.bodyMedium)
                Text(text = product.price ?: "", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
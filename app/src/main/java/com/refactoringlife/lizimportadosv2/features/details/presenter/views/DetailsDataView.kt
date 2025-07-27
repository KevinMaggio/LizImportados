package com.refactoringlife.lizimportadosv2.features.details.presenter.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary
import com.refactoringlife.lizimportadosv2.ui.theme.TextSecondary

@Composable
fun DetailsDataView(products: List<ProductResponse>) {
    val productPagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { products.size }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 30.dp, bottom = 90.dp),
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
                pageCount = { product.images?.size ?: 0 }
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
                        .weight(1.7f)
                ) { imagePage ->
                    Surface(
                        modifier = Modifier
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                            .background(Color.Red)
                    ) {
                        LipsyAsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            url = product.images?.get(imagePage) ?: ""
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(product.images?.size ?: 0) { index ->
                        val color =
                            if (imagePagerState.currentPage == index) TextBlue else Color.LightGray
                        val size =
                            if (imagePagerState.currentPage == index) 15.dp else 10.dp

                        Box(
                            modifier = Modifier
                                .size(size)
                                .clip(CircleShape)
                                .background(color)
                                .padding(2.dp)
                        )
                        if (index < (product.images?.size ?: 0) - 1) Spacer(modifier = Modifier.width(6.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 32.dp)
                )
                {
                    Text(
                        text = product.name.orEmpty(),
                        fontSize = 20.sp,
                        color = TextPrimary,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    )

                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            text = product.brand.orEmpty(),
                            fontSize = 14.sp,
                            color = TextSecondary,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Talle: "+ product.size.orEmpty(),
                            fontSize = 14.sp,
                            color = TextPrimary,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        )
                    }

                    LipsyDivider()

                    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = product.price?.toString().orEmpty(),
                            fontSize = 18.sp,
                            color = TextPrimary,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Agregar a carrito",
                            fontSize = 14.sp,
                            color = TextPrimary,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        )

                        Image(
                            painter = painterResource(R.drawable.icon_more),
                            contentDescription = "",
                            modifier =Modifier.padding(start = 10.dp).size(20.dp)

                        )
                    }

                    LipsyDivider()

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = product.description.orEmpty(),
                        fontSize = 14.sp,
                        color = TextPrimary,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    )
                }
            }
        }
    }
}
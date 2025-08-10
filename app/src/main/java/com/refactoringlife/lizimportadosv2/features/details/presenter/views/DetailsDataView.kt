package com.refactoringlife.lizimportadosv2.features.details.presenter.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyAsyncImage
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyZoomableImage
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
import com.refactoringlife.lizimportadosv2.core.utils.capitalizeWords
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary
import com.refactoringlife.lizimportadosv2.ui.theme.TextSecondary
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.google.common.io.Files.append

@Composable
fun DetailsDataView(
    products: List<ProductResponse>,
    onProductPageChanged: (Int) -> Unit = {},
    onAddToCart: (String) -> Unit = {},
    isAddingToCart: Boolean = false
) {
    var showZoomDialog by remember { mutableStateOf(false) }
    var selectedImageIndex by remember { mutableStateOf(0) }
    val productPagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { products.size }
    )

    // Detectar cambios de página para cargar más productos
    LaunchedEffect(productPagerState.currentPage) {
        onProductPageChanged(productPagerState.currentPage)
    }

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
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = imagePagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.7f)
                        .padding(horizontal = 20.dp)
                ) { imagePage ->
                    Box {
                        Surface(
                            modifier = Modifier
                                .shadow(elevation = 5.dp, shape = RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .clickable {
                                    selectedImageIndex = imagePage
                                    showZoomDialog = true
                                }
                        ) {
                            LipsyAsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                url = product.images?.get(imagePage) ?: ""
                            )
                        }

                        // Icono de zoom en la esquina
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .background(
                                    color = Color.Black.copy(alpha = 0.6f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_more),
                                contentDescription = "Zoom",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
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
                        if (index < (product.images?.size ?: 0) - 1
                        ) Spacer(modifier = Modifier.width(6.dp))
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 32.dp, start = 20.dp, end = 20.dp)
                )
                {
                    Text(
                        text = product.name.orEmpty().capitalizeWords(),
                        fontSize = 20.sp,
                        color = TextPrimary,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    )

                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            text = product.brand.orEmpty().capitalizeWords(),
                            fontSize = 14.sp,
                            color = TextSecondary,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "Talle: " + product.size.orEmpty(),
                            fontSize = 14.sp,
                            color = TextPrimary,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        )
                    }

                    LipsyDivider()

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$" + if (product.isOffer == true) {
                                product.offerPrice?.toString().orEmpty()
                            } else {
                                product.price?.toString().orEmpty()
                            },
                            fontSize = 18.sp,
                            color = TextBlue,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                enabled = !isAddingToCart
                            ) {
                                if (!isAddingToCart) {
                                    onAddToCart(product.id)
                                }
                            }
                        ) {
                            Text(
                                text = if (isAddingToCart) "Agregando..." else "Agregar a carrito",
                                fontSize = 14.sp,
                                color = if (isAddingToCart) Color.Gray else TextPrimary,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            )

                            Image(
                                painter = painterResource(R.drawable.icon_more),
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .size(20.dp)
                            )
                        }
                    }

                    LipsyDivider()

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                                    color = TextPrimary
                                )
                            ) {
                                append("Descripción: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.ExtraLight,
                                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                    color = TextSecondary
                                )
                            ) {
                                append(product.description.orEmpty().capitalizeWords())
                            }
                        },
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    // Diálogo de zoom
    if (showZoomDialog && products.isNotEmpty()) {
        val currentProduct = products[productPagerState.currentPage]
        currentProduct.images?.let { images ->
            LipsyZoomableImage(
                images = images,
                initialImageIndex = selectedImageIndex,
                onDismiss = { showZoomDialog = false }
            )
        }
    }
}
package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.ui.theme.CardBackGround
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary
import com.refactoringlife.lizimportadosv2.ui.theme.TextSecondary

@Composable
fun CartComboItem(
    cartComboModel: ProductCartModel.CartComboModel,
    onRemove: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Header del combo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🎁 COMBO ESPECIAL",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 12.sp,
                color = TextBlue
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Contenido del combo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Imágenes de los productos
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    // Primera imagen
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .width(50.dp)
                            .background(CardBackGround, RoundedCornerShape(8.dp))
                    ) {
                        if (cartComboModel.firstProduct.image.isNullOrEmpty()) {
                            Image(
                                painter = painterResource(R.drawable.icon_default_clothes),
                                contentDescription = "no image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        } else {
                            AsyncImage(
                                model = cartComboModel.firstProduct.image,
                                contentDescription = "product image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Icono +
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.LightGray, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Segunda imagen
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .width(50.dp)
                            .background(CardBackGround, RoundedCornerShape(8.dp))
                    ) {
                        if (cartComboModel.secondProduct.image.isNullOrEmpty()) {
                            Image(
                                painter = painterResource(R.drawable.icon_default_clothes),
                                contentDescription = "no image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        } else {
                            AsyncImage(
                                model = cartComboModel.secondProduct.image,
                                contentDescription = "product image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Botón Eliminar
                Text(
                    text = "Eliminar",
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 10.sp,
                    color = TextBlue,
                    modifier = Modifier.clickable { onRemove() }
                )
            }
            
            // Precios
            Column(
                horizontalAlignment = Alignment.End
            ) {
                // Precio tachado
                Text(
                    text = "$${cartComboModel.originalPrice}",
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                
                // Precio del combo
                Text(
                    text = "$${cartComboModel.comboPrice}",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 14.sp,
                    color = TextBlue
                )
                
                // Descuento
                if (cartComboModel.discount > 0) {
                    Text(
                        text = "Descuento: $${cartComboModel.discount}",
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                        fontSize = 10.sp,
                        color = TextBlue
                    )
                }
            }
        }
    }
} 
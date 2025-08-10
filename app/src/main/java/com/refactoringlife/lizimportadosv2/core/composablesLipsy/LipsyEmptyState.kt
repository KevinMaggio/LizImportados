package com.refactoringlife.lizimportadosv2.core.composablesLipsy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary

@Composable
fun LipsyEmptyState(
    icon: Int = R.drawable.icon_default_clothes,
    title: String,
    description: String,
    actionButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(40.dp)
        ) {
            // Ícono
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Empty state icon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 20.dp),
                alpha = 0.6f
            )
            
            // Título
            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 18.sp,
                color = TextBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Descripción
            Text(
                text = description,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                fontSize = 14.sp,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            
            // Botón de acción (opcional)
            actionButton?.let {
                Spacer(modifier = Modifier.height(24.dp))
                it()
            }
        }
    }
}

@Composable
fun LipsyEmptyProducts(
    category: String = "esta categoría",
    onExploreOtherCategories: (() -> Unit)? = null
) {
    LipsyEmptyState(
        icon = R.drawable.icon_default_clothes,
        title = "¡Oops! No hay productos aquí",
        description = "No encontramos productos en $category en este momento. ¡Pero no te preocupes! Tenemos muchas otras opciones esperándote.",
        actionButton = onExploreOtherCategories?.let { action ->
            {
                LipsyButtonSecondary(
                    text = "Explorar otras categorías",
                    onClick = action
                )
            }
        }
    )
}

@Composable
fun LipsyEmptyCart(
    onStartShopping: () -> Unit
) {
    LipsyEmptyState(
        icon = R.drawable.icon_cart,
        title = "Tu carrito está vacío",
        description = "¡Es hora de llenarlo con productos increíbles! Descubre nuestra colección de ropa importada de calidad.",
        actionButton = {
            LipsyButtonPrimary(
                text = "Comenzar a comprar",
                onClick = onStartShopping
            )
        }
    )
}

@Composable
fun LipsyProductNotFound(
    onGoBack: () -> Unit
) {
    LipsyEmptyState(
        icon = R.drawable.icon_default_clothes,
        title = "Producto no encontrado",
        description = "El producto que buscas no está disponible o fue removido de nuestro catálogo. Te invitamos a explorar otros productos similares.",
        actionButton = {
            LipsyButtonSecondary(
                text = "Volver atrás",
                onClick = onGoBack
            )
        }
    )
}

// Componentes de botones auxiliares
@Composable
private fun LipsyButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = TextBlue,
            contentColor = Color.White
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun LipsyButtonSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
            contentColor = TextBlue
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, TextBlue),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

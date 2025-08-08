package com.refactoringlife.lizimportadosv2.features.cart.presenter.views

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.refactoringlife.lizimportadosv2.R
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyDivider
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyMoreItems
import com.refactoringlife.lizimportadosv2.core.composablesLipsy.LipsyWhatsAppButton
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.core.dto.response.CartResponse
import com.refactoringlife.lizimportadosv2.ui.theme.TextBlue
import com.refactoringlife.lizimportadosv2.ui.theme.TextPrimary

@Composable
fun CartDataView(
    product: ProductCartModel,
    cartStatus: CartResponse.CartStatus = CartResponse.CartStatus.AVAILABLE,
    onRemoveItem: (String) -> Unit = {},
    onClearCart: () -> Unit = {}
) {
    val context = LocalContext.current
    val availableProducts = product.products.filter { it.available }
    val sealedProducts = product.products.filter { !it.available }

    fun sendWhatsAppMessage() {
        val message = buildString {
            appendLine("🛒 *Carrito de Compras - Liz Importados*")
            appendLine()
            appendLine("*Productos:*")
            availableProducts.forEach { item ->
                val displayPrice = if (item.isOffer && item.offerPrice != null) {
                    item.offerPrice
                } else {
                    item.price
                }
                val priceText = if (item.isOffer && item.offerPrice != null) {
                    "$${item.price} → $$displayPrice"
                } else {
                    "$$displayPrice"
                }
                appendLine("• ${item.name} - Talle: ${item.season} - $priceText")
            }
            appendLine()
            appendLine("*Resumen:*")
            appendLine("Subtotal: $${product.subTotal}")
            if (product.discount > 0) {
                appendLine("Descuento: $${product.discount}")
            }
            appendLine("*Total: $${product.total}*")
            appendLine()
            appendLine("Por favor, confirma mi pedido. ¡Gracias! 🛍️")
        }

        val encodedMessage = Uri.encode(message)
        val whatsappUrl = "https://wa.me/5401162399695?text=$encodedMessage"
        
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback: abrir WhatsApp con el número
            val fallbackUrl = "https://wa.me/5401162399695"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl))
            context.startActivity(intent)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        item {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Carrito de compras",
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 16.sp,
                color = TextBlue
            )
            Spacer(Modifier.height(20.dp))
        }

        // Mostrar mensaje si el carrito está en procesamiento
        if (cartStatus == CartResponse.CartStatus.PROCESSING) {
            item {
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "🔄 Tu carrito está siendo procesado",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 16.sp,
                    color = TextBlue,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "No puedes modificar tu carrito mientras se procesa tu pedido anterior. Te notificaremos cuando esté listo.",
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 14.sp,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
            }
        } else {
            // Mostrar productos solo si el carrito está disponible
            item {
                availableProducts.forEach { item ->
                    LipsyDivider()
                    CartItem(
                        cartItemModel = item,
                        onRemove = {
                            onRemoveItem(item.productId)
                        }
                    )
                }
            }

            item {
                Spacer(Modifier.height(20.dp))

                TotalSection(
                    subtotal = product.subTotal,
                    discount = product.discount,
                    total = product.total
                )
            }

            item {
                Spacer(Modifier.height(20.dp))

                LipsyWhatsAppButton(
                    action = { sendWhatsAppMessage() }
                )

                Spacer(Modifier.height(20.dp))
            }
        }

        if (sealedProducts.isNotEmpty()) {
            item {
                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Productos no disponibles",
                    fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                    fontSize = 16.sp,
                    color = TextBlue
                )

                Spacer(Modifier.height(20.dp))
            }

            item {
                sealedProducts.forEach { item ->
                    LipsyDivider()
                    CartItem(
                        cartItemModel = item,
                        onRemove = {
                            onRemoveItem(item.productId)
                        }
                    )
                }
            }

            item {
                Spacer(Modifier.height(20.dp))

                HorizontalDivider(color = Color.Black)

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Estos productos fueron vendidos mientras armabas el carrito, lo sentimos",
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    fontSize = 12.sp,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))
            }
        }

        item {
            Spacer(Modifier.height(20.dp))

            LipsyMoreItems(
                action = { /* TODO: Navegar a más productos */ }
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

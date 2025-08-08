package com.refactoringlife.lizimportadosv2.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.refactoringlife.lizimportadosv2.core.dto.response.CartResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await
import android.util.Log

class CartService(
    private val firestore: FirebaseFirestore = FirebaseProvider.instance,
    private val productService: ProductService = ProductService()
) {
    
    suspend fun getCart(email: String): CartResponse? {
        return try {
            val cartDoc = firestore.collection("carts")
                .document(email)
                .get()
                .await()

            if (!cartDoc.exists()) {
                return null
            }

            val products = cartDoc.get("products") as? List<Map<String, Any>> ?: emptyList()
            val cartProducts = products.mapNotNull { productMap ->
                try {
                    CartResponse.CartProductResponse(
                        productId = productMap["productId"] as? String ?: "",
                        name = productMap["name"] as? String ?: "",
                        image = productMap["image"] as? String ?: "",
                        season = productMap["season"] as? String ?: "",
                        available = productMap["available"] as? Boolean ?: false,
                        price = (productMap["price"] as? Long)?.toInt() ?: 0,
                        isOffer = productMap["isOffer"] as? Boolean ?: false,
                        offerPrice = (productMap["offerPrice"] as? Long)?.toInt(),
                        addedAt = (productMap["addedAt"] as? Long) ?: System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    null
                }
            }

            // Recalcular totales para asegurar consistencia
            val (recalculatedSubTotal, recalculatedDiscount, recalculatedTotal) = calculateCartTotals(cartProducts)
            
            Log.d("CartService", "📊 Totales recalculados - SubTotal: $recalculatedSubTotal, Discount: $recalculatedDiscount, Total: $recalculatedTotal")
            
            CartResponse(
                email = email,
                products = cartProducts,
                subTotal = recalculatedSubTotal,
                discount = recalculatedDiscount,
                total = recalculatedTotal,
                lastUpdated = cartDoc.getLong("lastUpdated") ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e("CartService", "Error obteniendo carrito para $email", e)
            null
        }
    }

    suspend fun addToCart(email: String, productId: String): CartAddResult {
        return try {
            Log.d("CartService", "🛒 Iniciando addToCart - Email: $email, ProductId: $productId")
            
            // Obtener el producto
            Log.d("CartService", "📦 Obteniendo producto con ID: $productId")
            val product = productService.getProductById(productId)
            Log.d("CartService", "✅ Producto obtenido: ${product.name}")
            
            // Obtener carrito actual
            Log.d("CartService", "🛒 Obteniendo carrito actual para: $email")
            val currentCart = getCart(email) ?: CartResponse(
                email = email,
                products = emptyList(),
                subTotal = 0,
                discount = 0,
                total = 0
            )
            Log.d("CartService", "📦 Carrito actual tiene ${currentCart.products.size} productos")

            // Verificar si el producto ya está en el carrito
            val existingProduct = currentCart.products.find { it.productId == productId }
            if (existingProduct != null) {
                Log.d("CartService", "⚠️ Producto $productId ya está en el carrito")
                return CartAddResult.AlreadyInCart(currentCart)
            }

            // Crear nuevo item del carrito
            val newCartProduct = CartResponse.CartProductResponse(
                productId = product.id,
                name = product.name ?: "",
                image = product.images?.firstOrNull() ?: "",
                season = product.size ?: "",
                available = product.isAvailable ?: false,
                price = product.price ?: 0,
                isOffer = product.isOffer ?: false,
                offerPrice = product.offerPrice
            )
            Log.d("CartService", "🆕 Nuevo item del carrito creado: ${newCartProduct.name}")

            // Agregar producto al carrito
            val updatedProducts = currentCart.products + newCartProduct
            val (newSubTotal, newDiscount, newTotal) = calculateCartTotals(updatedProducts)

            val updatedCart = CartResponse(
                email = email,
                products = updatedProducts,
                subTotal = newSubTotal,
                discount = newDiscount,
                total = newTotal
            )

            // Guardar en Firestore
            Log.d("CartService", "💾 Guardando carrito actualizado en Firestore")
            saveCart(updatedCart)

            Log.d("CartService", "✅ Producto $productId agregado al carrito de $email. Total productos: ${updatedCart.products.size}")
            CartAddResult.Success(updatedCart)
        } catch (e: Exception) {
            Log.e("CartService", "❌ Error agregando producto $productId al carrito de $email", e)
            CartAddResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun removeFromCart(email: String, productId: String): CartResponse? {
        return try {
            val currentCart = getCart(email) ?: return null

            val updatedProducts = currentCart.products.filter { it.productId != productId }
            val (newSubTotal, newDiscount, newTotal) = calculateCartTotals(updatedProducts)

            val updatedCart = CartResponse(
                email = email,
                products = updatedProducts,
                subTotal = newSubTotal,
                discount = newDiscount,
                total = newTotal
            )

            saveCart(updatedCart)

            Log.d("CartService", "Producto $productId removido del carrito de $email")
            updatedCart
        } catch (e: Exception) {
            Log.e("CartService", "Error removiendo producto $productId del carrito de $email", e)
            null
        }
    }

    suspend fun clearCart(email: String): Boolean {
        return try {
            firestore.collection("carts")
                .document(email)
                .delete()
                .await()

            Log.d("CartService", "Carrito de $email limpiado")
            true
        } catch (e: Exception) {
            Log.e("CartService", "Error limpiando carrito de $email", e)
            false
        }
    }

    private fun calculateCartTotals(products: List<CartResponse.CartProductResponse>): Triple<Int, Int, Int> {
        var subTotal = 0
        var totalDiscount = 0
        
        Log.d("CartService", "🧮 Calculando totales para ${products.size} productos")
        
        products.forEach { product ->
            // Subtotal siempre usa el precio original
            subTotal += product.price
            
            // Calcular descuento si está en oferta
            if (product.isOffer && product.offerPrice != null) {
                val productDiscount = product.price - product.offerPrice
                totalDiscount += productDiscount
                Log.d("CartService", "🏷️ Producto en oferta: ${product.name} - Original: ${product.price}, Oferta: ${product.offerPrice}, Descuento: $productDiscount")
            } else {
                Log.d("CartService", "💰 Producto normal: ${product.name} - Precio: ${product.price}")
            }
        }
        
        // Total = Subtotal - Descuento
        val total = subTotal - totalDiscount
        
        Log.d("CartService", "📊 Totales finales - SubTotal: $subTotal, Discount: $totalDiscount, Total: $total")
        
        return Triple(subTotal, totalDiscount, total)
    }

    private suspend fun saveCart(cart: CartResponse) {
        val cartData = hashMapOf(
            "email" to cart.email,
            "products" to cart.products.map { product ->
                hashMapOf(
                    "productId" to product.productId,
                    "name" to product.name,
                    "image" to product.image,
                    "season" to product.season,
                    "available" to product.available,
                    "price" to product.price,
                    "isOffer" to product.isOffer,
                    "offerPrice" to product.offerPrice,
                    "addedAt" to product.addedAt
                )
            },
            "subTotal" to cart.subTotal,
            "discount" to cart.discount,
            "total" to cart.total,
            "lastUpdated" to cart.lastUpdated
        )

        firestore.collection("carts")
            .document(cart.email)
            .set(cartData)
            .await()
    }

    sealed class CartAddResult {
        data class Success(val cart: CartResponse) : CartAddResult()
        data class AlreadyInCart(val cart: CartResponse) : CartAddResult()
        data class Error(val message: String) : CartAddResult()
    }
} 
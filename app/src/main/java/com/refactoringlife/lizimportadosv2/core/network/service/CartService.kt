package com.refactoringlife.lizimportadosv2.core.network.service

import com.google.firebase.firestore.FirebaseFirestore
import com.refactoringlife.lizimportadosv2.core.dto.response.CartResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.CartFullResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.core.network.fireStore.FirebaseProvider
import kotlinx.coroutines.tasks.await
import android.util.Log

class CartService(
    private val firestore: FirebaseFirestore = FirebaseProvider.instance,
    private val productService: ProductService = ProductService()
) {
    
    // Obtener carrito básico (solo IDs)
    suspend fun getCart(email: String): CartResponse? {
        return try {
            val cartDoc = firestore.collection("carts")
                .document(email)
                .get()
                .await()

            if (!cartDoc.exists()) {
                return null
            }

            val productIds = cartDoc.get("productIds") as? List<String> ?: emptyList()
            val statusString = cartDoc.getString("status") ?: "AVAILABLE"
            val status = CartResponse.CartStatus.valueOf(statusString)

            CartResponse(
                email = email,
                productIds = productIds,
                status = status,
                lastUpdated = cartDoc.getLong("lastUpdated") ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e("CartService", "Error obteniendo carrito para $email", e)
            null
        }
    }

    // Obtener carrito completo con productos calculados
    suspend fun getCartFull(email: String): CartFullResponse? {
        return try {
            val cart = getCart(email) ?: return null
            
            // Verificar estado del carrito
            if (cart.status == CartResponse.CartStatus.PROCESSING) {
                Log.d("CartService", "🔄 Carrito en procesamiento para: $email")
                return CartFullResponse(
                    email = email,
                    products = emptyList(),
                    subTotal = 0,
                    discount = 0,
                    total = 0,
                    status = cart.status,
                    lastUpdated = cart.lastUpdated
                )
            }

            // Obtener productos completos y validar disponibilidad
            val products = mutableListOf<CartFullResponse.CartProductResponse>()
            val invalidProductIds = mutableListOf<String>()
            
            cart.productIds.forEach { productId ->
                try {
                    val product = productService.getProductById(productId)
                    
                    // Validar que el producto existe y está disponible
                    if (product.isAvailable == true) {
                        val cartProduct = CartFullResponse.CartProductResponse(
                            productId = product.id,
                            name = product.name ?: "",
                            image = product.images?.firstOrNull() ?: "",
                            season = product.size ?: "",
                            available = true,
                            price = product.price ?: 0,
                            isOffer = product.isOffer ?: false,
                            offerPrice = product.offerPrice
                        )
                        products.add(cartProduct)
                        Log.d("CartService", "✅ Producto válido: ${product.name} - Precio: ${product.price}")
                    } else {
                        Log.d("CartService", "❌ Producto no disponible: $productId")
                        invalidProductIds.add(productId)
                    }
                } catch (e: Exception) {
                    Log.e("CartService", "❌ Error obteniendo producto $productId", e)
                    invalidProductIds.add(productId)
                }
            }

            // Remover productos inválidos del carrito si los hay
            if (invalidProductIds.isNotEmpty()) {
                Log.d("CartService", "🧹 Removiendo ${invalidProductIds.size} productos inválidos del carrito")
                val validProductIds = cart.productIds.filter { it !in invalidProductIds }
                val updatedCart = cart.copy(productIds = validProductIds)
                saveCart(updatedCart)
            }

            // Calcular totales con productos válidos
            val (subTotal, discount, total) = calculateCartTotals(products)

            Log.d("CartService", "📊 Carrito final - Productos válidos: ${products.size}, Inválidos removidos: ${invalidProductIds.size}")

            CartFullResponse(
                email = email,
                products = products,
                subTotal = subTotal,
                discount = discount,
                total = total,
                status = cart.status,
                lastUpdated = cart.lastUpdated
            )
        } catch (e: Exception) {
            Log.e("CartService", "Error obteniendo carrito completo para $email", e)
            null
        }
    }

    suspend fun addToCart(email: String, productId: String): CartAddResult {
        return try {
            Log.d("CartService", "🛒 Iniciando addToCart - Email: $email, ProductId: $productId")
            
            // Validar que el producto existe y está disponible ANTES de agregarlo
            try {
                val product = productService.getProductById(productId)
                if (product.isAvailable != true) {
                    Log.d("CartService", "❌ Producto $productId no está disponible")
                    return CartAddResult.Error("Este producto no está disponible en este momento")
                }
                Log.d("CartService", "✅ Producto validado: ${product.name} - Precio actual: ${product.price}")
            } catch (e: Exception) {
                Log.e("CartService", "❌ Producto $productId no existe", e)
                return CartAddResult.Error("Este producto ya no está disponible")
            }
            
            // Obtener carrito actual
            val currentCart = getCart(email) ?: CartResponse(
                email = email,
                productIds = emptyList(),
                status = CartResponse.CartStatus.AVAILABLE
            )

            // Verificar si el carrito está disponible
            if (currentCart.status == CartResponse.CartStatus.PROCESSING) {
                Log.d("CartService", "❌ Carrito en procesamiento, no se puede agregar productos")
                return CartAddResult.Error("El carrito está siendo procesado y no se puede modificar")
            }

            // Verificar si el producto ya está en el carrito
            if (currentCart.productIds.contains(productId)) {
                Log.d("CartService", "⚠️ Producto $productId ya está en el carrito")
                val fullCart = getCartFull(email)
                return CartAddResult.AlreadyInCart(fullCart)
            }

            // Agregar producto al carrito
            val updatedProductIds = currentCart.productIds + productId
            val updatedCart = CartResponse(
                email = email,
                productIds = updatedProductIds,
                status = currentCart.status
            )

            // Guardar en Firestore
            saveCart(updatedCart)

            // Obtener carrito completo para la respuesta
            val fullCart = getCartFull(email)
            
            Log.d("CartService", "✅ Producto $productId agregado al carrito de $email")
            CartAddResult.Success(fullCart)
        } catch (e: Exception) {
            Log.e("CartService", "❌ Error agregando producto $productId al carrito de $email", e)
            CartAddResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun removeFromCart(email: String, productId: String): CartFullResponse? {
        return try {
            val currentCart = getCart(email) ?: return null

            // Verificar si el carrito está disponible
            if (currentCart.status == CartResponse.CartStatus.PROCESSING) {
                Log.d("CartService", "❌ Carrito en procesamiento, no se puede remover productos")
                return null
            }

            val updatedProductIds = currentCart.productIds.filter { it != productId }
            val updatedCart = CartResponse(
                email = email,
                productIds = updatedProductIds,
                status = currentCart.status
            )

            saveCart(updatedCart)

            Log.d("CartService", "Producto $productId removido del carrito de $email")
            getCartFull(email)
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

    // Función para cambiar estado del carrito (para admin)
    suspend fun updateCartStatus(email: String, status: CartResponse.CartStatus): Boolean {
        return try {
            val currentCart = getCart(email) ?: return false
            
            val updatedCart = currentCart.copy(status = status)
            saveCart(updatedCart)
            
            Log.d("CartService", "Estado del carrito de $email actualizado a: $status")
            true
        } catch (e: Exception) {
            Log.e("CartService", "Error actualizando estado del carrito de $email", e)
            false
        }
    }

    // Función para limpiar productos inválidos de todos los carritos (llamada por admin)
    suspend fun cleanInvalidProductsFromAllCarts(): Boolean {
        return try {
            Log.d("CartService", "🧹 Iniciando limpieza de productos inválidos en todos los carritos")
            
            val cartsCollection = firestore.collection("carts")
            val cartDocs = cartsCollection.get().await()
            
            var cleanedCarts = 0
            var totalInvalidProducts = 0
            
            cartDocs.forEach { cartDoc ->
                try {
                    val email = cartDoc.getString("email") ?: return@forEach
                    val productIds = cartDoc.get("productIds") as? List<String> ?: emptyList()
                    
                    val validProductIds = mutableListOf<String>()
                    
                    productIds.forEach { productId ->
                        try {
                            val product = productService.getProductById(productId)
                            if (product.isAvailable == true) {
                                validProductIds.add(productId)
                            } else {
                                totalInvalidProducts++
                                Log.d("CartService", "❌ Producto inválido removido: $productId del carrito de $email")
                            }
                        } catch (e: Exception) {
                            totalInvalidProducts++
                            Log.d("CartService", "❌ Producto inexistente removido: $productId del carrito de $email")
                        }
                    }
                    
                    // Actualizar carrito si se removieron productos
                    if (validProductIds.size < productIds.size) {
                        val updatedCart = CartResponse(
                            email = email,
                            productIds = validProductIds,
                            status = CartResponse.CartStatus.valueOf(cartDoc.getString("status") ?: "AVAILABLE"),
                            lastUpdated = cartDoc.getLong("lastUpdated") ?: System.currentTimeMillis()
                        )
                        saveCart(updatedCart)
                        cleanedCarts++
                        Log.d("CartService", "✅ Carrito de $email limpiado: ${productIds.size - validProductIds.size} productos removidos")
                    }
                } catch (e: Exception) {
                    Log.e("CartService", "Error procesando carrito ${cartDoc.id}", e)
                }
            }
            
            Log.d("CartService", "🧹 Limpieza completada: $cleanedCarts carritos limpiados, $totalInvalidProducts productos inválidos removidos")
            true
        } catch (e: Exception) {
            Log.e("CartService", "Error limpiando productos inválidos", e)
            false
        }
    }

    private fun calculateCartTotals(products: List<CartFullResponse.CartProductResponse>): Triple<Int, Int, Int> {
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
            "productIds" to cart.productIds,
            "status" to cart.status.name,
            "lastUpdated" to cart.lastUpdated
        )

        firestore.collection("carts")
            .document(cart.email)
            .set(cartData)
            .await()
    }

    sealed class CartAddResult {
        data class Success(val cart: CartFullResponse?) : CartAddResult()
        data class AlreadyInCart(val cart: CartFullResponse?) : CartAddResult()
        data class Error(val message: String) : CartAddResult()
    }
} 

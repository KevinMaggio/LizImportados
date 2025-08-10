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
            val comboIds = cartDoc.get("comboIds") as? List<String> ?: emptyList()
            val statusString = cartDoc.getString("status") ?: "AVAILABLE"
            val status = CartResponse.CartStatus.valueOf(statusString)

            CartResponse(
                email = email,
                productIds = productIds,
                comboIds = comboIds,
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
            if (cart.status == CartResponse.CartStatus.PROCESSED) {
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

            // Obtener combos completos y validar disponibilidad
            val combos = mutableListOf<CartFullResponse.CartComboResponse>()
            val invalidComboIds = mutableListOf<String>()
            
            cart.comboIds.forEach { comboId ->
                try {
                    val combo = productService.getComboById(comboId)
                    
                    // Validar que el combo existe y está disponible
                    if (combo.available) {
                        // Verificar que ambos productos del combo estén disponibles
                        val firstProduct = try {
                            productService.getProductById(combo.firstProduct.id)
                        } catch (e: Exception) {
                            null
                        }
                        
                        val secondProduct = try {
                            productService.getProductById(combo.secondProduct.id)
                        } catch (e: Exception) {
                            null
                        }
                        
                        if (firstProduct?.isAvailable == true && secondProduct?.isAvailable == true) {
                            val cartCombo = CartFullResponse.CartComboResponse(
                                comboId = combo.id,
                                name = "Combo ${combo.firstProduct.brand} + ${combo.secondProduct.brand}",
                                firstProduct = CartFullResponse.CartProductResponse(
                                    productId = firstProduct.id,
                                    name = firstProduct.name ?: "",
                                    image = firstProduct.images?.firstOrNull() ?: "",
                                    season = firstProduct.size ?: "",
                                    available = true,
                                    price = firstProduct.price ?: 0,
                                    isOffer = firstProduct.isOffer ?: false,
                                    offerPrice = firstProduct.offerPrice
                                ),
                                secondProduct = CartFullResponse.CartProductResponse(
                                    productId = secondProduct.id,
                                    name = secondProduct.name ?: "",
                                    image = secondProduct.images?.firstOrNull() ?: "",
                                    season = secondProduct.size ?: "",
                                    available = true,
                                    price = secondProduct.price ?: 0,
                                    isOffer = secondProduct.isOffer ?: false,
                                    offerPrice = secondProduct.offerPrice
                                ),
                                originalPrice = combo.oldPrice,
                                comboPrice = combo.price,
                                discount = combo.oldPrice - combo.price,
                                available = true
                            )
                            combos.add(cartCombo)
                            Log.d("CartService", "✅ Combo válido: ${cartCombo.name} - Precio: ${combo.price}")
                        } else {
                            Log.d("CartService", "❌ Combo no disponible: $comboId - Productos no disponibles")
                            invalidComboIds.add(comboId)
                        }
                    } else {
                        Log.d("CartService", "❌ Combo no disponible: $comboId")
                        invalidComboIds.add(comboId)
                    }
                } catch (e: Exception) {
                    Log.e("CartService", "❌ Error obteniendo combo $comboId", e)
                    invalidComboIds.add(comboId)
                }
            }

            // Remover productos y combos inválidos del carrito si los hay
            if (invalidProductIds.isNotEmpty() || invalidComboIds.isNotEmpty()) {
                Log.d("CartService", "🧹 Removiendo ${invalidProductIds.size} productos y ${invalidComboIds.size} combos inválidos del carrito")
                val validProductIds = cart.productIds.filter { it !in invalidProductIds }
                val validComboIds = cart.comboIds.filter { it !in invalidComboIds }
                val updatedCart = cart.copy(productIds = validProductIds, comboIds = validComboIds)
                saveCart(updatedCart)
            }

            // Calcular totales con productos y combos válidos
            val (subTotal, discount, total) = calculateCartTotals(products, combos)

            Log.d("CartService", "📊 Carrito final - Productos válidos: ${products.size}, Combos válidos: ${combos.size}, Inválidos removidos: ${invalidProductIds.size + invalidComboIds.size}")

            CartFullResponse(
                email = email,
                products = products,
                combos = combos,
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
            if (currentCart.status == CartResponse.CartStatus.PROCESSED) {
                Log.d("CartService", "❌ Carrito en análisis, no se puede agregar productos")
                return CartAddResult.Error("Tu carrito está en análisis")
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
            if (currentCart.status == CartResponse.CartStatus.PROCESSED) {
                Log.d("CartService", "❌ Carrito en procesamiento, no se puede remover productos")
                return null
            }

            val updatedProductIds = currentCart.productIds.filter { it != productId }
            val updatedCart = CartResponse(
                email = email,
                productIds = updatedProductIds,
                comboIds = currentCart.comboIds,
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

    suspend fun removeComboFromCart(email: String, comboId: String): CartFullResponse? {
        return try {
            val currentCart = getCart(email) ?: return null

            // Verificar si el carrito está disponible
            if (currentCart.status == CartResponse.CartStatus.PROCESSED) {
                Log.d("CartService", "❌ Carrito en procesamiento, no se puede remover combos")
                return null
            }

            val updatedComboIds = currentCart.comboIds.filter { it != comboId }
            val updatedCart = CartResponse(
                email = email,
                productIds = currentCart.productIds,
                comboIds = updatedComboIds,
                status = currentCart.status
            )

            saveCart(updatedCart)

            Log.d("CartService", "Combo $comboId removido del carrito de $email")
            getCartFull(email)
        } catch (e: Exception) {
            Log.e("CartService", "Error removiendo combo $comboId del carrito de $email", e)
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

    private fun calculateCartTotals(
        products: List<CartFullResponse.CartProductResponse>,
        combos: List<CartFullResponse.CartComboResponse> = emptyList()
    ): Triple<Int, Int, Int> {
        var subTotal = 0
        var totalDiscount = 0
        
        Log.d("CartService", "🧮 Calculando totales para ${products.size} productos y ${combos.size} combos")
        
        // Calcular productos individuales
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
        
        // Calcular combos
        combos.forEach { combo ->
            // Subtotal usa el precio original del combo
            subTotal += combo.originalPrice
            
            // Descuento del combo
            totalDiscount += combo.discount
            Log.d("CartService", "🎁 Combo: ${combo.name} - Original: ${combo.originalPrice}, Combo: ${combo.comboPrice}, Descuento: ${combo.discount}")
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
            "comboIds" to cart.comboIds,
            "status" to cart.status.name,
            "lastUpdated" to cart.lastUpdated
        )

        firestore.collection("carts")
            .document(cart.email)
            .set(cartData)
            .await()
    }

    suspend fun addComboToCart(email: String, comboId: String): CartAddResult {
        return try {
            Log.d("CartService", "🛒 Iniciando addComboToCart - Email: $email, ComboId: $comboId")
            
            // Validar que el combo existe y está disponible ANTES de agregarlo
            try {
                val combo = productService.getComboById(comboId)
                if (!combo.available) {
                    Log.d("CartService", "❌ Combo $comboId no está disponible")
                    return CartAddResult.Error("Este combo no está disponible en este momento")
                }
                
                // Verificar que ambos productos del combo estén disponibles
                val firstProduct = productService.getProductById(combo.firstProduct.id)
                val secondProduct = productService.getProductById(combo.secondProduct.id)
                
                if (firstProduct.isAvailable != true || secondProduct.isAvailable != true) {
                    Log.d("CartService", "❌ Productos del combo no están disponibles")
                    return CartAddResult.Error("Los productos de este combo no están disponibles")
                }
                
                Log.d("CartService", "✅ Combo validado: ${combo.firstProduct.brand} + ${combo.secondProduct.brand} - Precio: ${combo.price}")
            } catch (e: Exception) {
                Log.e("CartService", "❌ Combo $comboId no existe", e)
                return CartAddResult.Error("Este combo ya no está disponible")
            }
            
            // Obtener carrito actual
            val currentCart = getCart(email) ?: CartResponse(
                email = email,
                productIds = emptyList(),
                comboIds = emptyList(),
                status = CartResponse.CartStatus.AVAILABLE
            )

            // Verificar si el carrito está disponible
            if (currentCart.status == CartResponse.CartStatus.PROCESSED) {
                Log.d("CartService", "❌ Carrito en análisis, no se puede agregar combos")
                return CartAddResult.Error("Tu carrito está en análisis")
            }

            // Verificar si el combo ya está en el carrito
            if (currentCart.comboIds.contains(comboId)) {
                Log.d("CartService", "⚠️ Combo $comboId ya está en el carrito")
                val fullCart = getCartFull(email)
                return CartAddResult.AlreadyInCart(fullCart)
            }

            // Agregar combo al carrito
            val updatedComboIds = currentCart.comboIds + comboId
            val updatedCart = CartResponse(
                email = email,
                productIds = currentCart.productIds,
                comboIds = updatedComboIds,
                status = currentCart.status
            )

            // Guardar en Firestore
            saveCart(updatedCart)

            // Obtener carrito completo para la respuesta
            val fullCart = getCartFull(email)
            
            Log.d("CartService", "✅ Combo $comboId agregado al carrito de $email")
            CartAddResult.Success(fullCart)
        } catch (e: Exception) {
            Log.e("CartService", "❌ Error agregando combo $comboId al carrito de $email", e)
            CartAddResult.Error(e.message ?: "Error desconocido")
        }
    }

    sealed class CartAddResult {
        data class Success(val cart: CartFullResponse?) : CartAddResult()
        data class AlreadyInCart(val cart: CartFullResponse?) : CartAddResult()
        data class Error(val message: String) : CartAddResult()
    }
} 

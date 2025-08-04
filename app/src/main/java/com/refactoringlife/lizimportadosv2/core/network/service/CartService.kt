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
                        addedAt = (productMap["addedAt"] as? Long) ?: System.currentTimeMillis()
                    )
                } catch (e: Exception) {
                    null
                }
            }

            CartResponse(
                email = email,
                products = cartProducts,
                subTotal = (cartDoc.getLong("subTotal") ?: 0).toInt(),
                discount = (cartDoc.getLong("discount") ?: 0).toInt(),
                total = (cartDoc.getLong("total") ?: 0).toInt(),
                lastUpdated = cartDoc.getLong("lastUpdated") ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e("CartService", "Error obteniendo carrito para $email", e)
            null
        }
    }

    suspend fun addToCart(email: String, productId: String): CartResponse? {
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
                return currentCart
            }

            // Crear nuevo item del carrito
            val newCartProduct = CartResponse.CartProductResponse(
                productId = product.id,
                name = product.name ?: "",
                image = product.images?.firstOrNull() ?: "",
                season = product.size ?: "",
                available = product.isAvailable ?: false,
                price = product.price ?: 0
            )
            Log.d("CartService", "🆕 Nuevo item del carrito creado: ${newCartProduct.name}")

            // Agregar producto al carrito
            val updatedProducts = currentCart.products + newCartProduct
            val newSubTotal = updatedProducts.sumOf { it.price }
            val newTotal = newSubTotal // Por ahora sin descuento

            val updatedCart = CartResponse(
                email = email,
                products = updatedProducts,
                subTotal = newSubTotal,
                discount = 0,
                total = newTotal
            )

            // Guardar en Firestore
            Log.d("CartService", "💾 Guardando carrito actualizado en Firestore")
            saveCart(updatedCart)

            Log.d("CartService", "✅ Producto $productId agregado al carrito de $email. Total productos: ${updatedCart.products.size}")
            updatedCart
        } catch (e: Exception) {
            Log.e("CartService", "❌ Error agregando producto $productId al carrito de $email", e)
            null
        }
    }

    suspend fun removeFromCart(email: String, productId: String): CartResponse? {
        return try {
            val currentCart = getCart(email) ?: return null

            val updatedProducts = currentCart.products.filter { it.productId != productId }
            val newSubTotal = updatedProducts.sumOf { it.price }
            val newTotal = newSubTotal

            val updatedCart = CartResponse(
                email = email,
                products = updatedProducts,
                subTotal = newSubTotal,
                discount = 0,
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
} 
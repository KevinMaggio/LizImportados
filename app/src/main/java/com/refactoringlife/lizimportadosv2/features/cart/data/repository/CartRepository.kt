package com.refactoringlife.lizimportadosv2.features.cart.data.repository

import com.refactoringlife.lizimportadosv2.core.dto.response.CartResponse
import com.refactoringlife.lizimportadosv2.core.network.service.CartService
import android.util.Log

// Alias para el resultado del carrito
typealias CartAddResult = CartService.CartAddResult

class CartRepository(
    private val cartService: CartService = CartService()
) {
    
    suspend fun getCart(email: String): CartResponse? {
        return try {
            Log.d("CartRepository", "🛒 Obteniendo carrito para: $email")
            val cart = cartService.getCart(email)
            if (cart != null) {
                Log.d("CartRepository", "✅ Carrito obtenido con ${cart.products.size} productos")
            } else {
                Log.d("CartRepository", "📭 No hay carrito para: $email")
            }
            cart
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error obteniendo carrito para: $email", e)
            null
        }
    }

    suspend fun addToCart(email: String, productId: String): CartAddResult {
        return try {
            Log.d("CartRepository", "➕ Agregando producto $productId al carrito")
            val result = cartService.addToCart(email, productId)
            when (result) {
                is CartService.CartAddResult.Success -> {
                    Log.d("CartRepository", "✅ Producto agregado. Total productos: ${result.cart.products.size}")
                }
                is CartService.CartAddResult.AlreadyInCart -> {
                    Log.d("CartRepository", "⚠️ Producto ya estaba en el carrito")
                }
                is CartService.CartAddResult.Error -> {
                    Log.e("CartRepository", "❌ Error agregando producto: ${result.message}")
                }
            }
            result
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error agregando producto al carrito", e)
            CartService.CartAddResult.Error(e.message ?: "Error desconocido")
        }
    }

    suspend fun removeFromCart(email: String, productId: String): CartResponse? {
        return try {
            Log.d("CartRepository", "➖ Removiendo producto $productId del carrito")
            val cart = cartService.removeFromCart(email, productId)
            if (cart != null) {
                Log.d("CartRepository", "✅ Producto removido. Total productos: ${cart.products.size}")
            }
            cart
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error removiendo producto del carrito", e)
            null
        }
    }

    suspend fun clearCart(email: String): Boolean {
        return try {
            Log.d("CartRepository", "🗑️ Limpiando carrito de $email")
            val success = cartService.clearCart(email)
            if (success) {
                Log.d("CartRepository", "✅ Carrito limpiado exitosamente")
            } else {
                Log.e("CartRepository", "❌ Error limpiando carrito")
            }
            success
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error limpiando carrito", e)
            false
        }
    }
}
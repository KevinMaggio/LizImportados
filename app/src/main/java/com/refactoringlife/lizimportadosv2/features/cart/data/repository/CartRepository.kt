package com.refactoringlife.lizimportadosv2.features.cart.data.repository

import com.refactoringlife.lizimportadosv2.core.network.service.CartService
import com.refactoringlife.lizimportadosv2.core.dto.response.CartFullResponse
import android.util.Log
import com.refactoringlife.lizimportadosv2.core.dto.response.CartResponse

class CartRepository(
    private val cartService: CartService = CartService()
) {
    
    suspend fun getCart(email: String): CartFullResponse? {
        return try {
            Log.d("CartRepository", "🛒 Obteniendo carrito completo para: $email")
            val cart = cartService.getCartFull(email)
            Log.d("CartRepository", "✅ Carrito obtenido: ${cart?.products?.size ?: 0} productos")
            cart
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error obteniendo carrito", e)
            null
        }
    }

    suspend fun addToCart(email: String, productId: String): CartService.CartAddResult {
        return try {
            Log.d("CartRepository", "➕ Agregando producto $productId al carrito")
            val result = cartService.addToCart(email, productId)
            Log.d("CartRepository", "📦 Resultado: $result")
            result
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error agregando producto al carrito", e)
            CartService.CartAddResult.Error("Error al agregar producto: ${e.message}")
        }
    }

    suspend fun addComboToCart(email: String, comboId: String): CartService.CartAddResult {
        return try {
            Log.d("CartRepository", "🎁 Agregando combo $comboId al carrito")
            val result = cartService.addComboToCart(email, comboId)
            Log.d("CartRepository", "📦 Resultado: $result")
            result
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error agregando combo al carrito", e)
            CartService.CartAddResult.Error("Error al agregar combo: ${e.message}")
        }
    }

    suspend fun removeFromCart(email: String, productId: String): CartFullResponse? {
        return try {
            Log.d("CartRepository", "➖ Removiendo producto $productId del carrito")
            val cart = cartService.removeFromCart(email, productId)
            Log.d("CartRepository", "✅ Producto removido del carrito")
            cart
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error removiendo producto del carrito", e)
            null
        }
    }

    suspend fun clearCart(email: String): Boolean {
        return try {
            Log.d("CartRepository", "🗑️ Limpiando carrito")
            val success = cartService.clearCart(email)
            Log.d("CartRepository", "✅ Carrito limpiado: $success")
            success
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error limpiando carrito", e)
            false
        }
    }

    suspend fun updateCartStatus(email: String, status: CartResponse.CartStatus): Boolean {
        return try {
            Log.d("CartRepository", "🔄 Actualizando estado del carrito a: $status")
            val success = cartService.updateCartStatus(email, status)
            Log.d("CartRepository", "✅ Estado actualizado: $success")
            success
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error actualizando estado del carrito", e)
            false
        }
    }

    suspend fun cleanInvalidProductsFromAllCarts(): Boolean {
        return try {
            Log.d("CartRepository", "🧹 Limpiando productos inválidos de todos los carritos")
            val success = cartService.cleanInvalidProductsFromAllCarts()
            Log.d("CartRepository", "✅ Limpieza completada: $success")
            success
        } catch (e: Exception) {
            Log.e("CartRepository", "❌ Error limpiando productos inválidos", e)
            false
        }
    }
}
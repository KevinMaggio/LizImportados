package com.refactoringlife.lizimportadosv2.core.dto.response

data class CartResponse(
    val email: String,
    val productIds: List<String>,
    val status: CartStatus = CartStatus.AVAILABLE,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    enum class CartStatus {
        AVAILABLE,    // Carrito disponible para editar
        PROCESSING    // Carrito en procesamiento (no editable)
    }
}

// Respuesta completa con productos calculados
data class CartFullResponse(
    val email: String,
    val products: List<CartProductResponse>,
    val subTotal: Int,
    val discount: Int,
    val total: Int,
    val status: CartResponse.CartStatus,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    data class CartProductResponse(
        val productId: String,
        val name: String,
        val image: String,
        val season: String,
        val available: Boolean,
        val price: Int,
        val isOffer: Boolean = false,
        val offerPrice: Int? = null,
        val addedAt: Long = System.currentTimeMillis()
    )
} 
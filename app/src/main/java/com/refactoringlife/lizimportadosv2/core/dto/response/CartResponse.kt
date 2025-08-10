package com.refactoringlife.lizimportadosv2.core.dto.response

data class CartResponse(
    val email: String,
    val productIds: List<String>,
    val comboIds: List<String> = emptyList(), // Nuevo: IDs de combos
    val status: CartStatus = CartStatus.AVAILABLE,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    enum class CartStatus {
        AVAILABLE,    // Carrito disponible para editar
        PROCESSED     // Carrito procesado/analizado (no editable)
    }
}

// Respuesta completa con productos calculados
data class CartFullResponse(
    val email: String,
    val products: List<CartProductResponse>,
    val combos: List<CartComboResponse> = emptyList(), // Nuevo: combos completos
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
    
    // Nuevo: Respuesta para combos en el carrito
    data class CartComboResponse(
        val comboId: String,
        val name: String,
        val firstProduct: CartProductResponse,
        val secondProduct: CartProductResponse,
        val originalPrice: Int, // Suma de precios originales
        val comboPrice: Int,    // Precio del combo
        val discount: Int,      // Descuento del combo
        val available: Boolean,
        val addedAt: Long = System.currentTimeMillis()
    )
} 
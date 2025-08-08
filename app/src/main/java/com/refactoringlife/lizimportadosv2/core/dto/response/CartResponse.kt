package com.refactoringlife.lizimportadosv2.core.dto.response

data class CartResponse(
    val email: String,
    val products: List<CartProductResponse>,
    val subTotal: Int,
    val discount: Int,
    val total: Int,
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
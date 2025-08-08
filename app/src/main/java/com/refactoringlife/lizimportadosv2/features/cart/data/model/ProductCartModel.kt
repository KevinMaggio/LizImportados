package com.refactoringlife.lizimportadosv2.features.cart.data.model

data class ProductCartModel (
    val subTotal:Int,
    val discount: Int,
    val total:Int,
    val products: List<CartItemModel>,
    val combos: List<CartComboModel> = emptyList()
){
    data class CartItemModel(
        val productId: String,
        val image: String?,
        val name: String?,
        val season: String?,
        val available: Boolean,
        val price: Int,
        val isOffer: Boolean = false,
        val offerPrice: Int? = null
    )
    
    data class CartComboModel(
        val comboId: String,
        val name: String,
        val firstProduct: CartItemModel,
        val secondProduct: CartItemModel,
        val originalPrice: Int,
        val comboPrice: Int,
        val discount: Int,
        val available: Boolean
    )
}
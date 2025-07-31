package com.refactoringlife.lizimportadosv2.features.cart.data.model

data class ProductCartModel (
    val subTotal:Int,
    val discount: Int,
    val total:Int,
    val products: List<CartItemModel>
){
    data class CartItemModel(
        val image: String?,
        val name: String?,
        val season: String?,
        val available: Boolean,
        val price: Int
    )
}
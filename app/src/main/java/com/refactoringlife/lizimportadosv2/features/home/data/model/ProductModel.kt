package com.refactoringlife.lizimportadosv2.features.home.data.model

data class ProductModel(
    val id: String,
    val images: List<String>,
    val brand: String?,
    val name: String?,
    val description: String?,
    val offersPrice: String?,
    val isOffer: Boolean,
    val size: String?,
    val price: String?,
)
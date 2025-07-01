package com.refactoringlife.lizimportados.features.home.data.model

data class ProductModel(
    val id: String,
    var url: String?,
    val title: String?,
    val subtitle: String?,
    val oldPrice: String?,
    val price: String?,
)
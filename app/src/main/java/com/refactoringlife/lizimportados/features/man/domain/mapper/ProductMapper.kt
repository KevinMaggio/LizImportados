package com.refactoringlife.lizimportados.features.man.domain.mapper

import com.refactoringlife.lizimportados.core.dto.response.ProductResponse
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel

fun ProductResponse.toDomain(): ProductModel {
    return ProductModel(
        id = this.id,
        images = this.images ?: emptyList(),
        brand = this.brand,
        name = this.name,
        size = null, // No viene en el response
        description = this.description,
        offersPrice = this.offerPrice.toString(),
        price = this.price?.toString()
    )
} 
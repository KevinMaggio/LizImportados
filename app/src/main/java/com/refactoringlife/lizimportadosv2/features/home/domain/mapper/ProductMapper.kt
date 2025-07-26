package com.refactoringlife.lizimportadosv2.features.home.domain.mapper

import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel

fun ProductResponse.toProductModel(): ProductModel {
    return ProductModel(
        id = this.id,
        images = this.images ?: emptyList(),
        brand = this.brand,
        name = this.name,
        description = this.description,
        size = this.size,
        offersPrice = this.offerPrice.toString(),
        price = this.price?.toString()
    )
} 
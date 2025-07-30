package com.refactoringlife.lizimportadosv2.core.dto.request

import kotlinx.serialization.SerialName

class ComboRequest (
    @SerialName("old_price")
    val oldPrice: Int,
    @SerialName("price")
    val price: Int,
    @SerialName("first_product")
    val firstProduct: ComboProductResponse,
    @SerialName("second_product")
    val secondProduct: ComboProductResponse
)

data class ComboProductResponse(
    @SerialName("id")
    val id: String,
    @SerialName("brand")
    val brand: String,
    @SerialName("description")
    val description: String,
    @SerialName("image")
    val image: String
)
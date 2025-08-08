package com.refactoringlife.lizimportadosv2.core.dto.response

import kotlinx.serialization.SerialName

data class ComboResponse(
    @SerialName("id")
    val id: String = "", // Nuevo: ID único del combo
    @SerialName("available")
    val available: Boolean,
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
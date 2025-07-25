package com.refactoringlife.lizimportados.core.dto.response

import kotlinx.serialization.SerialName

data class ConfigResponse(
    @SerialName("is_offers")
    val isOffers: Boolean = false,
    @SerialName("options")
    val circleOptions: List<Option> = emptyList(),
    @SerialName("combo")
    val combos: List<ComboModel>? = null,
    // Aquí se pueden agregar más flags en el futuro
) {
    data class Option(
        @SerialName("name")
        val name: String,
        @SerialName("image")
        val image: String
    )

    data class ComboModel(
        @SerialName("old_price")
        val oldPrice: Int,
        @SerialName("price")
        val price: Int,
        @SerialName("first_product")
        val firstProduct: ComboProductModel,
        @SerialName("second_product")
        val secondProduct: ComboProductModel
    )

    data class ComboProductModel(
        @SerialName("id")
        val id: String?,
        @SerialName("brand")
        val brand: String?,
        @SerialName("description")
        val description: String,
        @SerialName("image")
        val image: String
    )

}
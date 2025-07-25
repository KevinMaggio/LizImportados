package com.refactoringlife.lizimportados.core.dto.response

import kotlinx.serialization.SerialName

data class ConfigResponse(
    @SerialName("is_offers")
    val isOffers: Boolean = false,
    @SerialName("options")
    val circleOptions: List<Option> = emptyList(),
    @SerialName("combo")
    val combos: List<Combo>? = null,
    // Aquí se pueden agregar más flags en el futuro
) {
    data class Option(
        @SerialName("name")
        val name: String,
        @SerialName("image")
        val image: String
    )

    data class Combo(
        @SerialName("show_combo")
        val showCombo: Boolean = false,
        @SerialName("combo_id")
        val comboID: String? = null,
    )
}
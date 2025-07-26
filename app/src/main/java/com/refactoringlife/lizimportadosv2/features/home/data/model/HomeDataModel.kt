package com.refactoringlife.lizimportadosv2.features.home.data.model

import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse

data class HomeDataModel (
    val config: ConfigResponse,
    val offersProducts: List<ProductModel>? = null,
    val comboProduct: List<ConfigResponse.ComboModel>? = null
)
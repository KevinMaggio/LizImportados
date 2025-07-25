package com.refactoringlife.lizimportados.features.home.data.model

import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse

data class HomeDataModel (
    val config: ConfigResponse,
    val offersProducts: List<ProductModel>? = null,
    val comboProduct: CombosModel? = null
)
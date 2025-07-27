package com.refactoringlife.lizimportadosv2.features.home.data.model

import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboResponse

data class HomeDataModel (
    val config: ConfigResponse,
    val offersProducts: List<ProductModel> = emptyList(),
    val comboProduct: List<ComboResponse> = emptyList()
)
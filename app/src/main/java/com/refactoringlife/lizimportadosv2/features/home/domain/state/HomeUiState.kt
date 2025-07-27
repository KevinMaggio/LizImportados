package com.refactoringlife.lizimportadosv2.features.home.domain.state

import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.ComboResponse
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel

data class HomeUiState(
    val config: ConfigResponse? = null,
    val offersProducts: List<ProductModel> = emptyList(),
    val combosModel: List<ComboResponse> = emptyList(),
    val showError: Boolean = false,
    val showLoading: Boolean = false,
    val errorMessage: String? = null
)
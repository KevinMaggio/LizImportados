package com.refactoringlife.lizimportadosv2.features.home.domain.state

import com.refactoringlife.lizimportadosv2.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel

data class HomeUiState(
    val config: ConfigResponse? = null,
    val offersProducts: List<ProductModel>? = null,
    val combosModel: List<ConfigResponse.ComboModel>? = null,
    val showError: Boolean = false,
    val showLoading: Boolean = false,
    val errorMessage: String? = null
)
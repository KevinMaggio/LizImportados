package com.refactoringlife.lizimportados.features.home.domain.state

import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel

data class HomeUiState(
    val config: ConfigResponse? = null,
    val offersProducts: List<ProductModel>? = null,
    val combosModel: List<ConfigResponse.ComboModel>? = null,
    val showError: Boolean = false,
    val showLoading: Boolean = false,
    val errorMessage: String? = null
)
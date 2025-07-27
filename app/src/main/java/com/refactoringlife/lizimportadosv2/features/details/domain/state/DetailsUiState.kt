package com.refactoringlife.lizimportadosv2.features.details.domain.state

import com.refactoringlife.lizimportadosv2.core.dto.response.ProductResponse

data class DetailsUiState(
    val mainProduct: ProductResponse? = null,
    val relatedProducts: List<ProductResponse> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreProducts: Boolean = true,
    val error: String? = null,
    val lastDocumentId: String? = null
) 
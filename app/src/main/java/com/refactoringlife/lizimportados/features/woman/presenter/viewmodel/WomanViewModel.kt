package com.refactoringlife.lizimportados.features.woman.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.woman.domain.usecase.GetWomanProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WomanUiState(
    val products: List<ProductModel>? = null,
    val showError: Boolean = false,
    val showLoading: Boolean = false
)

class WomanViewModel(
    private val getWomanProductsUseCase: GetWomanProductsUseCase = GetWomanProductsUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(WomanUiState())
    val state = _state.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(showLoading = true)
            
            when (val result = getWomanProductsUseCase()) {
                is Either.Success -> {
                    _state.value = _state.value.copy(
                        products = result.value,
                        showError = false,
                        showLoading = false
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        showError = true,
                        showLoading = false
                    )
                }
            }
        }
    }
} 
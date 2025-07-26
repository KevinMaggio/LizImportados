package com.refactoringlife.lizimportadosv2.features.man.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.features.man.domain.usecase.GetMenProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ManUiState(
    val products: List<ProductModel>? = null,
    val showError: Boolean = false,
    val showLoading: Boolean = false
)

class ManViewModel(
    private val getMenProductsUseCase: GetMenProductsUseCase = GetMenProductsUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(ManUiState())
    val state = _state.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(showLoading = true)
            
            when (val result = getMenProductsUseCase()) {
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
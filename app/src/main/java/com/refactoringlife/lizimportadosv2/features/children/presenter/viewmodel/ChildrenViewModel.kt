package com.refactoringlife.lizimportadosv2.features.children.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportadosv2.features.home.data.model.ProductModel
import com.refactoringlife.lizimportadosv2.features.children.domain.usecase.GetChildrenProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChildrenUiState(
    val products: List<ProductModel>? = null,
    val showError: Boolean = false,
    val showLoading: Boolean = false
)

class ChildrenViewModel(
    private val getChildrenProductsUseCase: GetChildrenProductsUseCase = GetChildrenProductsUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(ChildrenUiState())
    val state = _state.asStateFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(showLoading = true)
            
            when (val result = getChildrenProductsUseCase()) {
                is Either.Success -> {
                    _state.value = _state.value.copy(
                        products = result.value,
                        showError = false,
                        showLoading = false
                    )
                }
                is Either.Error -> {
                    _state.value = _state.value.copy(
                        showError = true,
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
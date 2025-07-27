package com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportadosv2.features.details.domain.state.DetailsUiState
import com.refactoringlife.lizimportadosv2.features.details.domain.usecase.GetDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getDetailsUseCase: GetDetailsUseCase = GetDetailsUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state = _state.asStateFlow()

    fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            // Cargar producto principal
            when (val productResult = getDetailsUseCase.getProductDetails(productId)) {
                is Either.Success -> {
                    _state.value = _state.value.copy(
                        mainProduct = productResult.value,
                        isLoading = false
                    )
                    
                    // Cargar productos relacionados
                    loadInitialRelatedProducts()
                }
                is Either.Error -> {
                    _state.value = _state.value.copy(
                        error = productResult.value,
                        isLoading = false
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        error = "Error desconocido",
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun loadInitialRelatedProducts() {
        when (val relatedResult = getDetailsUseCase.getInitialRelatedProducts()) {
            is Either.Success -> {
                _state.value = _state.value.copy(
                    relatedProducts = relatedResult.value,
                    hasMoreProducts = relatedResult.value.isNotEmpty()
                )
            }
            is Either.Error -> {
                _state.value = _state.value.copy(
                    error = relatedResult.value
                )
            }
            else -> {
                _state.value = _state.value.copy(
                    error = "Error al cargar productos relacionados"
                )
            }
        }
    }

    fun loadMoreProducts() {
        if (_state.value.isLoadingMore || !_state.value.hasMoreProducts) return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoadingMore = true)

            when (val moreResult = getDetailsUseCase.getMoreProducts(_state.value.lastDocumentId)) {
                is Either.Success -> {
                    val newProducts = _state.value.relatedProducts + moreResult.value
                    _state.value = _state.value.copy(
                        relatedProducts = newProducts,
                        isLoadingMore = false,
                        hasMoreProducts = moreResult.value.isNotEmpty()
                    )
                }
                is Either.Error -> {
                    _state.value = _state.value.copy(
                        error = moreResult.value,
                        isLoadingMore = false
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        error = "Error al cargar más productos",
                        isLoadingMore = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
} 
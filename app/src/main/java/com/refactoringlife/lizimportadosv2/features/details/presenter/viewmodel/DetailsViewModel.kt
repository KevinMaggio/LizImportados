package com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportadosv2.features.details.domain.state.DetailsUiState
import com.refactoringlife.lizimportadosv2.features.details.domain.usecase.GetDetailsUseCase
import com.refactoringlife.lizimportadosv2.features.details.data.repository.DetailsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class DetailsViewModel(
    private val getDetailsUseCase: GetDetailsUseCase = GetDetailsUseCase(),
    private val repository: DetailsRepository = DetailsRepository.getInstance()
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state = _state.asStateFlow()

    fun loadProductDetails(productId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                // Cargar producto principal
                when (val productResult = getDetailsUseCase.getProductDetails(productId)) {
                    is Either.Success -> {
                        val mainProduct = productResult.value
                        _state.value = _state.value.copy(
                            mainProduct = mainProduct,
                            isLoading = false
                        )
                        
                        // Cargar productos aleatorios
                        loadRandomProducts(productId)
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
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al cargar detalles: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, mainProduct = null)
            
            try {
                val products = repository.loadProductsByCategory(category)
                _state.value = _state.value.copy(
                    relatedProducts = products,
                    isLoading = false,
                    hasMoreProducts = repository.hasMoreProducts()
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al cargar productos: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private suspend fun loadRandomProducts(excludeProductId: String) {
        try {
            val products = repository.loadRandomProducts(excludeProductId)
            _state.value = _state.value.copy(
                relatedProducts = products,
                hasMoreProducts = repository.hasMoreProducts()
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = "Error al cargar productos relacionados: ${e.message}"
            )
        }
    }

    fun onProductPageChanged(currentIndex: Int) {
        viewModelScope.launch {
            val currentProducts = _state.value.relatedProducts
            val lastIndex = currentProducts.size - 1
            
            // Cargar más productos cuando el usuario llegue al último elemento del array actual
            if (currentIndex >= lastIndex && !_state.value.isLoadingMore && repository.hasMoreProducts()) {
                _state.value = _state.value.copy(isLoadingMore = true)
                
                try {
                    val newProducts = repository.loadMoreProducts(currentIndex)
                    
                    if (newProducts.isNotEmpty()) {
                        val updatedProducts = currentProducts + newProducts
                        _state.value = _state.value.copy(
                            relatedProducts = updatedProducts,
                            isLoadingMore = false
                        )
                    } else {
                        _state.value = _state.value.copy(
                            hasMoreProducts = false,
                            isLoadingMore = false
                        )
                    }
                } catch (e: Exception) {
                    _state.value = _state.value.copy(
                        isLoadingMore = false,
                        error = "Error al cargar más productos: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearProducts()
    }
} 
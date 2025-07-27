package com.refactoringlife.lizimportadosv2.features.details.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportadosv2.features.details.domain.state.DetailsUiState
import com.refactoringlife.lizimportadosv2.features.details.domain.usecase.GetDetailsUseCase
import com.refactoringlife.lizimportadosv2.features.details.data.repository.DetailsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getDetailsUseCase: GetDetailsUseCase = GetDetailsUseCase(),
    private val repository: DetailsRepository = DetailsRepository.getInstance()
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state = _state.asStateFlow()

    init {
        // Observar el Flow del repository
        viewModelScope.launch {
            repository.productsFlow.collectLatest { products ->
                _state.value = _state.value.copy(
                    relatedProducts = products,
                    hasMoreProducts = repository.hasMoreProducts(),
                    isLoadingMore = repository.isLoading()
                )
            }
        }
    }

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
                    repository.loadInitialProducts()
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

    fun onProductPageChanged(currentIndex: Int) {
        viewModelScope.launch {
            repository.loadMoreProductsIfNeeded(currentIndex)
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
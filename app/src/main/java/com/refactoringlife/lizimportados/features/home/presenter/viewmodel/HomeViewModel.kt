package com.refactoringlife.lizimportados.features.home.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportados.core.dto.response.ConfigResponse
import com.refactoringlife.lizimportados.features.home.data.model.ProductModel
import com.refactoringlife.lizimportados.features.home.domain.usecase.GetHomeDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val config: ConfigResponse? = null,
    val offersProducts: List<ProductModel>? = null,
    val comboProducts: List<ProductModel>? = null,
    val showError: Boolean = false,
    val showLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val getHomeDataUseCase: GetHomeDataUseCase = GetHomeDataUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(showLoading = true)

            when (val result = getHomeDataUseCase.getHomeData()) {
                is Either.Success -> {
                    _state.value = _state.value.copy(
                        config = result.value.config,
                        offersProducts = result.value.offersProducts,
                        comboProducts = result.value.comboProduct,
                        showError = false,
                        showLoading = false
                    )
                }
                is Either.Error -> {
                    _state.value = _state.value.copy(
                        showError = true,
                        showLoading = false,
                        errorMessage = result.value
                    )
                }
                else -> {
                    _state.value = _state.value.copy(
                        showError = true,
                        showLoading = false,
                        errorMessage = "Error desconocido"
                    )
                }
            }
        }
    }
} 
package com.refactoringlife.lizimportadosv2.features.cart.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.refactoringlife.lizimportadosv2.core.dto.response.CartFullResponse
import com.refactoringlife.lizimportadosv2.core.dto.response.CartResponse
import com.refactoringlife.lizimportadosv2.features.cart.data.model.ProductCartModel
import com.refactoringlife.lizimportadosv2.features.cart.data.repository.CartRepository
import com.refactoringlife.lizimportadosv2.core.network.service.CartService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class CartViewModel(
    private val repository: CartRepository = CartRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(CartUiState())
    val state = _state.asStateFlow()

    fun loadCart(email: String) {
        viewModelScope.launch {
            Log.d("CartViewModel", "🚀 Cargando carrito para: $email")
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val cartResponse = repository.getCart(email)
                if (cartResponse != null) {
                    val cartModel = mapToCartModel(cartResponse)
                    _state.value = _state.value.copy(
                        cart = cartModel,
                        isLoading = false,
                        cartStatus = cartResponse.status
                    )
                    Log.d("CartViewModel", "✅ Carrito cargado con ${cartModel.products.size} productos, estado: ${cartResponse.status}")
                } else {
                    // Carrito vacío
                    val emptyCart = ProductCartModel(
                        subTotal = 0,
                        discount = 0,
                        total = 0,
                        products = emptyList()
                    )
                    _state.value = _state.value.copy(
                        cart = emptyCart,
                        isLoading = false,
                        cartStatus = CartResponse.CartStatus.AVAILABLE
                    )
                    Log.d("CartViewModel", "📭 Carrito vacío para: $email")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Error cargando carrito", e)
                _state.value = _state.value.copy(
                    error = "Error al cargar el carrito: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun addToCart(email: String, productId: String) {
        viewModelScope.launch {
            Log.d("CartViewModel", "➕ Agregando producto $productId al carrito")
            _state.value = _state.value.copy(isAddingToCart = true)
            
            try {
                val result = repository.addToCart(email, productId)
                Log.d("CartViewModel", "📦 Resultado recibido: $result")
                
                when (result) {
                    is CartService.CartAddResult.Success -> {
                        val cartModel = result.cart?.let { mapToCartModel(it) }
                        val message = "✓ Producto agregado al carrito"
                        Log.d("CartViewModel", "✅ Estableciendo mensaje: '$message'")
                        _state.value = _state.value.copy(
                            cart = cartModel ?: _state.value.cart,
                            isAddingToCart = false,
                            addToCartMessage = message,
                            cartStatus = result.cart?.status ?: _state.value.cartStatus
                        )
                        Log.d("CartViewModel", "✅ Producto agregado al carrito")
                    }
                    is CartService.CartAddResult.AlreadyInCart -> {
                        val cartModel = result.cart?.let { mapToCartModel(it) }
                        val message = "⚠️ Este producto ya está en tu carrito"
                        Log.d("CartViewModel", "⚠️ Estableciendo mensaje: '$message'")
                        _state.value = _state.value.copy(
                            cart = cartModel ?: _state.value.cart,
                            isAddingToCart = false,
                            addToCartMessage = message,
                            cartStatus = result.cart?.status ?: _state.value.cartStatus
                        )
                        Log.d("CartViewModel", "⚠️ Producto ya estaba en el carrito")
                    }
                    is CartService.CartAddResult.Error -> {
                        val message = "❌ Error: ${result.message}"
                        Log.d("CartViewModel", "❌ Estableciendo mensaje: '$message'")
                        _state.value = _state.value.copy(
                            isAddingToCart = false,
                            addToCartMessage = message
                        )
                        Log.e("CartViewModel", "❌ Error agregando producto: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Error agregando producto al carrito", e)
                val message = "❌ Error al agregar producto"
                Log.d("CartViewModel", "❌ Estableciendo mensaje de error: '$message'")
                _state.value = _state.value.copy(
                    isAddingToCart = false,
                    addToCartMessage = message
                )
            }
        }
    }

    fun addComboToCart(email: String, comboId: String) {
        viewModelScope.launch {
            Log.d("CartViewModel", "🎁 Agregando combo $comboId al carrito")
            _state.value = _state.value.copy(isAddingToCart = true)
            
            try {
                val result = repository.addComboToCart(email, comboId)
                Log.d("CartViewModel", "📦 Resultado recibido: $result")
                
                when (result) {
                    is CartService.CartAddResult.Success -> {
                        val cartModel = result.cart?.let { mapToCartModel(it) }
                        val message = "✓ Combo agregado al carrito"
                        Log.d("CartViewModel", "✅ Estableciendo mensaje: '$message'")
                        _state.value = _state.value.copy(
                            cart = cartModel ?: _state.value.cart,
                            isAddingToCart = false,
                            addToCartMessage = message,
                            cartStatus = result.cart?.status ?: _state.value.cartStatus
                        )
                        Log.d("CartViewModel", "✅ Combo agregado al carrito")
                    }
                    is CartService.CartAddResult.AlreadyInCart -> {
                        val cartModel = result.cart?.let { mapToCartModel(it) }
                        val message = "⚠️ Este combo ya está en tu carrito"
                        Log.d("CartViewModel", "⚠️ Estableciendo mensaje: '$message'")
                        _state.value = _state.value.copy(
                            cart = cartModel ?: _state.value.cart,
                            isAddingToCart = false,
                            addToCartMessage = message,
                            cartStatus = result.cart?.status ?: _state.value.cartStatus
                        )
                        Log.d("CartViewModel", "⚠️ Combo ya estaba en el carrito")
                    }
                    is CartService.CartAddResult.Error -> {
                        val message = "❌ Error: ${result.message}"
                        Log.d("CartViewModel", "❌ Estableciendo mensaje: '$message'")
                        _state.value = _state.value.copy(
                            isAddingToCart = false,
                            addToCartMessage = message
                        )
                        Log.e("CartViewModel", "❌ Error agregando combo: ${result.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Error agregando combo al carrito", e)
                val message = "❌ Error al agregar combo"
                Log.d("CartViewModel", "❌ Estableciendo mensaje de error: '$message'")
                _state.value = _state.value.copy(
                    isAddingToCart = false,
                    addToCartMessage = message
                )
            }
        }
    }

    fun removeFromCart(email: String, productId: String) {
        viewModelScope.launch {
            Log.d("CartViewModel", "➖ Removiendo producto $productId del carrito")
            try {
                val cartResponse = repository.removeFromCart(email, productId)
                if (cartResponse != null) {
                    val cartModel = mapToCartModel(cartResponse)
                    _state.value = _state.value.copy(
                        cart = cartModel,
                        cartStatus = cartResponse.status
                    )
                    Log.d("CartViewModel", "✅ Producto removido del carrito")
                } else {
                    Log.e("CartViewModel", "❌ Error removiendo producto del carrito")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Error removiendo producto del carrito", e)
            }
        }
    }

    fun removeComboFromCart(email: String, comboId: String) {
        viewModelScope.launch {
            Log.d("CartViewModel", "🎁➖ Removiendo combo $comboId del carrito")
            try {
                val cartResponse = repository.removeComboFromCart(email, comboId)
                if (cartResponse != null) {
                    val cartModel = mapToCartModel(cartResponse)
                    _state.value = _state.value.copy(
                        cart = cartModel,
                        cartStatus = cartResponse.status
                    )
                    Log.d("CartViewModel", "✅ Combo removido del carrito")
                } else {
                    Log.e("CartViewModel", "❌ Error removiendo combo del carrito")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Error removiendo combo del carrito", e)
            }
        }
    }

    fun clearCart(email: String) {
        viewModelScope.launch {
            Log.d("CartViewModel", "🗑️ Limpiando carrito")
            try {
                val success = repository.clearCart(email)
                if (success) {
                    val emptyCart = ProductCartModel(
                        subTotal = 0,
                        discount = 0,
                        total = 0,
                        products = emptyList()
                    )
                    _state.value = _state.value.copy(
                        cart = emptyCart,
                        cartStatus = CartResponse.CartStatus.AVAILABLE
                    )
                    Log.d("CartViewModel", "✅ Carrito limpiado exitosamente")
                } else {
                    Log.e("CartViewModel", "❌ Error limpiando carrito")
                }
            } catch (e: Exception) {
                Log.e("CartViewModel", "❌ Error limpiando carrito", e)
            }
        }
    }

    fun clearAddToCartMessage() {
        _state.value = _state.value.copy(addToCartMessage = null)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun mapToCartModel(cartResponse: CartFullResponse): ProductCartModel {
        val cartItems = cartResponse.products.map { product ->
            ProductCartModel.CartItemModel(
                productId = product.productId,
                image = product.image,
                name = product.name,
                season = product.season,
                available = product.available,
                price = product.price,
                isOffer = product.isOffer,
                offerPrice = product.offerPrice
            )
        }
        
        val cartCombos = cartResponse.combos.map { combo ->
            ProductCartModel.CartComboModel(
                comboId = combo.comboId,
                name = combo.name,
                firstProduct = ProductCartModel.CartItemModel(
                    productId = combo.firstProduct.productId,
                    image = combo.firstProduct.image,
                    name = combo.firstProduct.name,
                    season = combo.firstProduct.season,
                    available = combo.firstProduct.available,
                    price = combo.firstProduct.price,
                    isOffer = combo.firstProduct.isOffer,
                    offerPrice = combo.firstProduct.offerPrice
                ),
                secondProduct = ProductCartModel.CartItemModel(
                    productId = combo.secondProduct.productId,
                    image = combo.secondProduct.image,
                    name = combo.secondProduct.name,
                    season = combo.secondProduct.season,
                    available = combo.secondProduct.available,
                    price = combo.secondProduct.price,
                    isOffer = combo.secondProduct.isOffer,
                    offerPrice = combo.secondProduct.offerPrice
                ),
                originalPrice = combo.originalPrice,
                comboPrice = combo.comboPrice,
                discount = combo.discount,
                available = combo.available
            )
        }

        return ProductCartModel(
            subTotal = cartResponse.subTotal,
            discount = cartResponse.discount,
            total = cartResponse.total,
            products = cartItems,
            combos = cartCombos
        )
    }
}

data class CartUiState(
    val cart: ProductCartModel = ProductCartModel(
        subTotal = 0,
        discount = 0,
        total = 0,
        products = emptyList()
    ),
    val isLoading: Boolean = false,
    val isAddingToCart: Boolean = false,
    val error: String? = null,
    val addToCartMessage: String? = null,
    val cartStatus: CartResponse.CartStatus = CartResponse.CartStatus.AVAILABLE
) 
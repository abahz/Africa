package com.abahz.africa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahz.africa.model.CartItem
import com.abahz.africa.model.Carts
import com.abahz.africa.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    private val _carts = MutableStateFlow<List<Carts>>(emptyList())
    val carts: StateFlow<List<Carts>> = _carts.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCarts(shopId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _carts.value = repository.getCartsByShop(shopId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadCartItems(cartId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _cartItems.value = repository.getCartItems(cartId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addCart(cart: Carts) {
        viewModelScope.launch {
            try {
                repository.insertCart(cart)
                loadCarts(cart.shopid)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun addItemToCart(item: CartItem) {
        viewModelScope.launch {
            try {
                repository.insertCartItem(item)
                loadCartItems(item.cid)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun removeItemFromCart(itemId: String, cartId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCartItem(itemId)
                loadCartItems(cartId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

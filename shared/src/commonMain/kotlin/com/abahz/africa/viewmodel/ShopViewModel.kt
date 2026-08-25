package com.abahz.africa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahz.africa.model.Shop
import com.abahz.africa.repository.ShopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShopViewModel(private val repository: ShopRepository) : ViewModel() {

    private val _shops = MutableStateFlow<List<Shop>>(emptyList())
    val shops: StateFlow<List<Shop>> = _shops.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentShop = MutableStateFlow<Shop?>(null)
    val currentShop: StateFlow<Shop?> = _currentShop.asStateFlow()

    init {
        loadShops()
    }

    fun signIn(phone: String, admin: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val shop = repository.signIn(phone, admin)
                if (shop != null) {
                    _currentShop.value = shop
                    _error.value = null
                    onResult(true)
                } else {
                    _error.value = "Identifiants invalides"
                    onResult(false)
                }
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false)
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadShops() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _shops.value = repository.getShops()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addShop(shop: Shop) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.insertShop(shop)
                _error.value = null
                loadShops()
            } catch (e: Exception) {
                val message = e.message ?: ""
                _error.value = when {
                    message.contains("23505") -> "Ce numéro de téléphone est déjà utilisé."
                    else -> message
                }
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateShop(shop: Shop) {
        viewModelScope.launch {
            try {
                repository.updateShop(shop)
                loadShops()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteShop(shopId: String) {
        viewModelScope.launch {
            try {
                repository.deleteShop(shopId)
                loadShops()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

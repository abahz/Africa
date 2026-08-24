package com.abahz.africa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahz.africa.model.Products
import com.abahz.africa.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Products>>(emptyList())
    val products: StateFlow<List<Products>> = _products.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _products.value = repository.getProducts()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadProductsByShop(shopId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _products.value = repository.getProductsByShop(shopId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addProduct(product: Products, imageBytes: ByteArray? = null, refreshByShop: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            try {
                var finalProduct = product
                if (imageBytes != null) {
                    val fileName = "${product.shopid}_${product.name}_${product.created}.jpg"
                    val imageUrl = repository.uploadImage(imageBytes, fileName)
                    finalProduct = product.copy(image = imageUrl)
                }
                repository.insertProduct(finalProduct)
                if (refreshByShop != null) loadProductsByShop(refreshByShop) else loadProducts()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateProduct(product: Products, imageBytes: ByteArray? = null, refreshByShop: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            try {
                var finalProduct = product
                if (imageBytes != null) {
                    val fileName = "${product.shopid}_${product.name}_${product.created}.jpg"
                    val imageUrl = repository.uploadImage(imageBytes, fileName)
                    finalProduct = product.copy(image = imageUrl)
                }
                repository.updateProduct(finalProduct)
                if (refreshByShop != null) loadProductsByShop(refreshByShop) else loadProducts()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun deleteProduct(product: Products, refreshByShop: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Optionally delete image from storage if needed
                 if (product.image.isNotEmpty()) {
                    val fileName = product.image.substringAfterLast("/")
                    repository.deleteImage(fileName)
                 }
                repository.deleteProduct(product.id)
                if (refreshByShop != null) loadProductsByShop(refreshByShop) else loadProducts()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}

package com.abahz.africa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahz.africa.model.Category
import com.abahz.africa.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCategories(shopId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _categories.value = repository.getCategoriesByShop(shopId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch {
            try {
                repository.insertCategory(category)
                loadCategories(category.shopid)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            try {
                repository.updateCategory(category)
                loadCategories(category.shopid)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteCategory(categoryId: String, shopId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCategory(categoryId)
                loadCategories(shopId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

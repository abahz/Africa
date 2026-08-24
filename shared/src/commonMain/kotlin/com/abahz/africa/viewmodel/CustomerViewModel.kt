package com.abahz.africa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahz.africa.model.Customer
import com.abahz.africa.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {

    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCustomers(shopId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _customers.value = repository.getCustomersByShop(shopId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                repository.insertCustomer(customer)
                loadCustomers(customer.shopid)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            try {
                repository.updateCustomer(customer)
                loadCustomers(customer.shopid)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteCustomer(customerId: String, shopId: String) {
        viewModelScope.launch {
            try {
                repository.deleteCustomer(customerId)
                loadCustomers(shopId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

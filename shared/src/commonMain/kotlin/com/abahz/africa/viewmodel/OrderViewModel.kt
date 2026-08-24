package com.abahz.africa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abahz.africa.model.OrderItem
import com.abahz.africa.model.Orders
import com.abahz.africa.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    private val _orders = MutableStateFlow<List<Orders>>(emptyList())
    val orders: StateFlow<List<Orders>> = _orders.asStateFlow()

    private val _orderItems = MutableStateFlow<List<OrderItem>>(emptyList())
    val orderItems: StateFlow<List<OrderItem>> = _orderItems.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadOrders() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _orders.value = repository.getOrders()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadOrderItems(orderId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _orderItems.value = repository.getOrderItems(orderId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun placeOrder(order: Orders, items: List<OrderItem>) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val createdOrder = repository.insertOrder(order)
                val itemsWithOrderId = items.map { it.copy(oid = createdOrder.id.toString()) }
                repository.insertOrderItems(itemsWithOrderId)
                loadOrders()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}

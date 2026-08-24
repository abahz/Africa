package com.abahz.africa.repository

import com.abahz.africa.model.OrderItem
import com.abahz.africa.model.Orders
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val supabaseClient: SupabaseClient) {

    private val ordersTable = "orders"
    private val orderItemsTable = "order_items"

    // Orders
    suspend fun getOrders(): List<Orders> = withContext(Dispatchers.Default) {
        supabaseClient.from(ordersTable).select().decodeList<Orders>()
    }

    suspend fun insertOrder(order: Orders): Orders = withContext(Dispatchers.Default) {
        supabaseClient.from(ordersTable).insert(order) {
            select()
        }.decodeSingle<Orders>()
    }

    // Order Items
    suspend fun getOrderItems(orderId: String): List<OrderItem> = withContext(Dispatchers.Default) {
        supabaseClient.from(orderItemsTable).select {
            filter {
                eq("oid", orderId)
            }
        }.decodeList<OrderItem>()
    }

    suspend fun insertOrderItems(items: List<OrderItem>) = withContext(Dispatchers.Default) {
        supabaseClient.from(orderItemsTable).insert(items)
    }
}

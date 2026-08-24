package com.abahz.africa.repository

import com.abahz.africa.model.CartItem
import com.abahz.africa.model.Carts
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository(private val supabaseClient: SupabaseClient) {

    private val cartsTable = "carts"
    private val cartItemsTable = "cart_items"

    // Carts
    suspend fun getCarts(): List<Carts> = withContext(Dispatchers.Default) {
        supabaseClient.from(cartsTable).select().decodeList<Carts>()
    }

    suspend fun insertCart(cart: Carts): Carts = withContext(Dispatchers.Default) {
        supabaseClient.from(cartsTable).insert(cart) {
            select()
        }.decodeSingle<Carts>()
    }

    // Cart Items
    suspend fun getCartItems(cartId: String): List<CartItem> = withContext(Dispatchers.Default) {
        supabaseClient.from(cartItemsTable).select {
            filter {
                eq("cid", cartId)
            }
        }.decodeList<CartItem>()
    }

    suspend fun insertCartItem(item: CartItem) = withContext(Dispatchers.Default) {
        supabaseClient.from(cartItemsTable).insert(item)
    }

    suspend fun deleteCartItem(itemId: String) = withContext(Dispatchers.Default) {
        supabaseClient.from(cartItemsTable).delete {
            filter {
                eq("id", itemId)
            }
        }
    }
}

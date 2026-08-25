package com.abahz.africa.repository

import com.abahz.africa.model.Shop
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShopRepository(private val supabaseClient: SupabaseClient) {

    private val table = "shops"

    suspend fun getShops(): List<Shop> = withContext(Dispatchers.Default) {
        supabaseClient.from(table).select().decodeList<Shop>()
    }

    suspend fun insertShop(shop: Shop) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).insert(shop)
    }

    suspend fun updateShop(shop: Shop) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).update(shop) {
            filter {
                eq("id", shop.id?:"")
            }
        }
    }

    suspend fun deleteShop(shopId: String) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).delete {
            filter {
                eq("id", shopId)
            }
        }
    }

    suspend fun signIn(phone: String, password: String): Shop? = withContext(Dispatchers.Default) {
        supabaseClient.from(table).select {
            filter {
                eq("phone", phone)
                eq("admin", password)
            }
        }.decodeSingleOrNull<Shop>()
    }
}

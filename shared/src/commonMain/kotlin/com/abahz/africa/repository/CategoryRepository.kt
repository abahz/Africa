package com.abahz.africa.repository

import com.abahz.africa.model.Category
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val supabaseClient: SupabaseClient) {

    private val table = "category"

    suspend fun getCategoriesByShop(shopId: String): List<Category> = withContext(Dispatchers.Default) {
        supabaseClient.from(table).select {
            filter {
                eq("shopid", shopId)
            }
        }.decodeList<Category>()
    }

    suspend fun insertCategory(category: Category) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).insert(category)
    }

    suspend fun updateCategory(category: Category) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).update(category) {
            filter {
                eq("id", category.id)
            }
        }
    }

    suspend fun deleteCategory(categoryId: String) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).delete {
            filter {
                eq("id", categoryId)
            }
        }
    }
}

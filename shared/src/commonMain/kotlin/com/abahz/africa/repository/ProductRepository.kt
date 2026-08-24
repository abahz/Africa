package com.abahz.africa.repository

import com.abahz.africa.model.Products
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(private val supabaseClient: SupabaseClient) {

    private val table = "products"
    private val bucket = "food-images"

    suspend fun getProducts(): List<Products> = withContext(Dispatchers.Default) {
        supabaseClient.from(table).select().decodeList<Products>()
    }

    suspend fun insertProduct(product: Products) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).insert(product)
    }

    suspend fun updateProduct(product: Products) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).update(product) {
            filter {
                eq("id", product.id)
            }
        }
    }

    suspend fun deleteProduct(productId: String) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).delete {
            filter {
                eq("id", productId)
            }
        }
    }

    // Storage Operations
    suspend fun uploadImage(bytes: ByteArray, fileName: String): String = withContext(Dispatchers.Default) {
        val storage = supabaseClient.storage.from(bucket)
        storage.upload(fileName, bytes) {
            upsert = true
        }
        storage.publicUrl(fileName)
    }

    suspend fun deleteImage(fileName: String) = withContext(Dispatchers.Default) {
        supabaseClient.storage.from(bucket).delete(fileName)
    }
}

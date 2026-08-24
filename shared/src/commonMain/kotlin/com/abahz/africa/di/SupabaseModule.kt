package com.abahz.africa.di

import com.abahz.africa.repository.CartRepository
import com.abahz.africa.repository.CustomerRepository
import com.abahz.africa.repository.OrderRepository
import com.abahz.africa.repository.ProductRepository
import com.abahz.africa.repository.ShopRepository
import com.abahz.africa.viewmodel.CartViewModel
import com.abahz.africa.viewmodel.CustomerViewModel
import com.abahz.africa.viewmodel.OrderViewModel
import com.abahz.africa.viewmodel.ProductViewModel
import com.abahz.africa.viewmodel.ShopViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(SupabaseInternal::class)
val supabaseModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl ="https://rghcwouuqdhbxqljrang.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJnaGN3b3V1cWRoYnhxbGpyYW5nIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODY3NTc0MjIsImV4cCI6MjEwMjMzMzQyMn0.k63j-Ej8j8zmfJAqYAwqokugAf-CrEyR8SWjPm0VG1E"
        ) {
            httpConfig {
                install(HttpTimeout) {
                    requestTimeoutMillis = 30000
                    connectTimeoutMillis = 30000
                    socketTimeoutMillis = 30000
                }
            }
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }
    // Shops
    singleOf(::ShopRepository)
    viewModelOf(::ShopViewModel)

    // Products
    singleOf(::ProductRepository)
    viewModelOf(::ProductViewModel)

    // Orders
    singleOf(::OrderRepository)
    viewModelOf(::OrderViewModel)

    // Carts
    singleOf(::CartRepository)
    viewModelOf(::CartViewModel)

    // Customers
    singleOf(::CustomerRepository)
    viewModelOf(::CustomerViewModel)
}

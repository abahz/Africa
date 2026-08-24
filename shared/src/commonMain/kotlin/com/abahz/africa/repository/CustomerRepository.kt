package com.abahz.africa.repository

import com.abahz.africa.model.Customer
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CustomerRepository(private val supabaseClient: SupabaseClient) {

    private val table = "customer"

    suspend fun getCustomers(): List<Customer> = withContext(Dispatchers.Default) {
        supabaseClient.from(table).select().decodeList<Customer>()
    }

    suspend fun insertCustomer(customer: Customer) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).insert(customer)
    }

    suspend fun updateCustomer(customer: Customer) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).update(customer) {
            filter {
                eq("id", customer.id)
            }
        }
    }

    suspend fun deleteCustomer(customerId: String) = withContext(Dispatchers.Default) {
        supabaseClient.from(table).delete {
            filter {
                eq("id", customerId)
            }
        }
    }

    suspend fun getCustomerByPhone(phone: String): Customer? = withContext(Dispatchers.Default) {
        supabaseClient.from(table).select {
            filter {
                eq("phone", phone)
            }
        }.decodeSingleOrNull<Customer>()
    }
}

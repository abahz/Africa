package com.abahz.africa.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Shop(
    val id: String = "",
    val phone: String = "",
    val name: String = "",
    val owner: String = "",
    val admin: String = "",
    val rccm: String = "",
    val idNat: String = "",
    val address: String = "",
    val created: Long = 0,
    val updated: Long = 0
)




@Serializable
data class Products(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val desc: String? = null,
    val type: String = ProductType.BOIS,
    val price: Long = 0,
    val coast: Long = 0,
    val qty: Double = 0.0,
    val category: String = "",
    val unity: String = "",
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

object ProductType {
    const val PIZZA = "Pizza"
    const val TACOS = "Tacos"
    const val DESSERT = "Dessert"
    const val BOIS = "Boissons"
}

@Serializable
data class Category(
    val id: String = "",
    val name: String = "",
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class Carts(
    val id: String = "",
    val pid: String = "",
    val qty: Double = 0.0,
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0,
)

@Serializable
data class CartItem(
    val id: String = "",
    val pid: String = "",
    val cid: String = "",
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0,
)

@Serializable
data class Orders(
    val id: Int = 0,
    val fid: Int = 0,
    val customer: String = "",
    val total: Long = 0,
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class OrderItem(
    val id: String = "",
    val pid: String = "",
    val oid: String = "",
    val qty: String = "",
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class Customer(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "Customer",
    val status: String = "Active",
    val phone: String = "",
    val address: String = "",
    val notes: Int = 0,
    val shopid: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

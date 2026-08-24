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
    @Transient val idNat: String = "",
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
    val qty: Double = 0.0,
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
data class Carts(
    val id: String = "",
    val pid: String = "",
    val qty: Double = 0.0,
    val created: Long = 0,
    val updated: Long = 0,
)

@Serializable
data class CartItem(
    val id: String = "",
    val pid: String = "",
    val cid: String = "",
    val created: Long = 0,
    val updated: Long = 0,
)

@Serializable
data class Orders(
    val id: Int = 0,
    val fid: Int = 0,
    val customer: String = "",
    val total: Long = 0,
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class OrderItem(
    val id: String = "",
    val pid: String = "",
    val oid: String = "",
    val qty: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class Customer(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val password: String = "",
    val address: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

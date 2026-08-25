package com.abahz.africa.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Shop(
    val id: String? = null,
    val phone: String = "",
    val name: String = "",
    val owner: String? = null,
    val admin: String = "",
    val rccm: String? = null,
    @Transient val idNat: String = "",
    val address: String? = null,
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class Products(
    val id: String? = null,
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
    val id: String? = null,
    val pid: String = "",
    val qty: Double = 0.0,
    val created: Long = 0,
    val updated: Long = 0,
)

@Serializable
data class CartItem(
    val id: String? = null,
    val pid: String = "",
    val cid: String = "",
    val created: Long = 0,
    val updated: Long = 0,
)

@Serializable
data class Orders(
    val id: String? = null,
    val fid: Int = 0,
    val customer: String? = null,
    val total: Long = 0,
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class OrderItem(
    val id: String? = null,
    val pid: String = "",
    val oid: String = "",
    val qty: String = "",
    val created: Long = 0,
    val updated: Long = 0
)

@Serializable
data class Customer(
    val id: String? = null,
    val name: String? = null,
    val phone: String = "",
    val password: String = "",
    val address: String? = null,
    val created: Long = 0,
    val updated: Long = 0
)

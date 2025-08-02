package com.example.campusbite.model

data class OrderHistoryItem(
    var orderId: String = "", // order unique id
    var foodName: String? = null,
    var foodPrice: String? = null,
    var foodImage: String? = null,
    var foodQuantity: String? = null,
    var orderTime: Long = 0L // order timestamp, optional
)

package com.example.campusbite.model

data class OrderDetailsFirebase(
    var userUid: String? = null,
    var userName: String? = null,
    var foodName: MutableList<String>? = null,
    var foodPrice: MutableList<String>? = null,
    var foodImage: MutableList<String>? = null,
    var foodQuantities: MutableList<Int>? = null,
    var address: String? = null,
    var totalPrice: String? = null,
    var phoneNumber: String? = null,
    var orderAccepted: Boolean = false,
    var paymentReceived: Boolean = false,
    var itemPushKey: String? = null,
    var currentTime: Long = 0
)

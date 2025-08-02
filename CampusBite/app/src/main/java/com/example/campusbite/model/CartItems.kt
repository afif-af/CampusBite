package com.example.campusbite.model

data class CartItems(
    var itemId: String = "",
    val foodName: String ?=null,
    val foodPrice: String ?=null,
    val foodDescription: String ?=null,
    val foodImage: String ?=null,
    var foodQuantity: String ?=null

)

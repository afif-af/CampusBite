package com.example.campusbite.model

data class CartItems(
    val foodName: String? =null,
    val foodPrice: String? =null,
    val foodDescription: String? =null,
    val foodImage: String? =null,
    var foodQuantity: Int? =null,
    var foodIngredients: String? =null
)


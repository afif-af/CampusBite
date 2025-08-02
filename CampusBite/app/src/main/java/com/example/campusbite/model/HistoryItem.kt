package com.example.campusbite.model

data class HistoryItem(
    val foodName: String = "",
    val foodPrice: String = "",
    val foodImageUrl: String = ""  // যদি Firebase এ URL থাকে, নাহলে drawable রিসোর্স id দিতে পারো int হিসেবেও
)

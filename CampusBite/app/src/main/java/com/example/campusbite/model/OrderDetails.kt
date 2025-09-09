package com.example.campusbite.model

import android.os.Parcel
import android.os.Parcelable

class OrderDetails() : Parcelable {

    var userUid: String? = null
    var userName: String? = null
    var foodName: MutableList<String> = mutableListOf()
    var foodPrice: MutableList<String> = mutableListOf()
    var foodImage: MutableList<String> = mutableListOf()
    var foodQuantities: MutableList<Int> = mutableListOf()

    var address: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(
        userId: String,
        name: String,
        foodItemName: ArrayList<String>,
        foodItemPrice: ArrayList<String>,
        foodItemImage: ArrayList<String>,
        foodItemQuantities: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phone: String,
        time: Long,
        itemPushKey: String?,
        orderAccepted: Boolean,
        paymentReceived: Boolean
    ) : this() {
        this.userUid = userId
        this.userName = name
        this.foodName = foodItemName
        this.foodPrice = foodItemPrice
        this.foodImage = foodItemImage
        this.foodQuantities = foodItemQuantities
        this.address = address
        this.totalPrice = totalAmount
        this.phoneNumber = phone
        this.orderAccepted = orderAccepted
        this.paymentReceived = paymentReceived
        this.itemPushKey = itemPushKey
        this.currentTime = time
    }

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        foodName = parcel.createStringArrayList() ?: mutableListOf()
        foodPrice = parcel.createStringArrayList() ?: mutableListOf()
        foodImage = parcel.createStringArrayList() ?: mutableListOf()
        foodQuantities = mutableListOf<Int>().apply { parcel.readList(this, Int::class.java.classLoader) }
        address = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeStringList(foodName)
        parcel.writeStringList(foodPrice)
        parcel.writeStringList(foodImage)
        parcel.writeList(foodQuantities)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails = OrderDetails(parcel)
        override fun newArray(size: Int): Array<OrderDetails?> = arrayOfNulls(size)
    }
}

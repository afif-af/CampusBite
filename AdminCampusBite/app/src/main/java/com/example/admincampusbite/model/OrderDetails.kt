package com.example.admincampusbite.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
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

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        foodName = parcel.createStringArrayList() ?: mutableListOf()
        foodPrice = parcel.createStringArrayList() ?: mutableListOf()
        foodImage = parcel.createStringArrayList() ?: mutableListOf()
        foodQuantities = parcel.createIntArray()?.toMutableList() ?: mutableListOf()
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
        parcel.writeIntArray(foodQuantities.toIntArray())
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
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}

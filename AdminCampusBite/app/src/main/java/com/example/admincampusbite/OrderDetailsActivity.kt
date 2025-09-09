package com.example.admincampusbite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.databinding.ActivityOrderDetailsBinding
import com.example.admincampusbite.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding
    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var foodName: MutableList<String> = mutableListOf()
    private var foodImages: MutableList<String> = mutableListOf()
    private var foodQuantity: MutableList<Int> = mutableListOf()
    private var foodPrices: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getParcelableExtra<OrderDetails>("UserOrderDetails")
        if (receivedOrderDetails != null) {
            userName = receivedOrderDetails.userName
            foodName=receivedOrderDetails.foodName!!
            foodImages=receivedOrderDetails.foodImage!!
            foodQuantity=receivedOrderDetails.foodQuantities!!
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            foodPrices=receivedOrderDetails.foodPrice!!
            totalPrice = receivedOrderDetails.totalPrice

            setUserDetail()
            setAdapter()
        }


    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this,foodName,foodImages,foodQuantity,foodPrices)
        binding.orderDetailsRecyclerView.adapter = adapter
    }

    private fun setUserDetail() {
        binding.name.text = userName
        binding.address.text=address
        binding.phone.text=phoneNumber
        binding.totalPay.text=totalPrice

    }

}

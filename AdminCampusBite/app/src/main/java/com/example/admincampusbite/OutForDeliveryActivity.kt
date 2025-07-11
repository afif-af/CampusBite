package com.example.admincampusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.adapter.DeliveryAdapter
import com.example.admincampusbite.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton3.setOnClickListener {
            onBackPressed()
        }

        val customerName=arrayListOf(
            "Jone Doe",
            "Jone Smith",
            "Mike jonson"
        )
        val moneyStatus=arrayListOf(
            "received",
            "notReceived",
            "pending"
        )
        val adapter= DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter=adapter
        binding.deliveryRecyclerView.layoutManager= LinearLayoutManager(this)


    }
}
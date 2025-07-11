package com.example.admincampusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.adapter.DeliveryAdapter
import com.example.admincampusbite.adapter.PendingOrderAdapter
import com.example.admincampusbite.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {

    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton5.setOnClickListener {
            onBackPressed()
        }

        val orderCustomerName=arrayListOf(
            "Jone Doe",
            "Jone Smith",
            "Mike jonson"
        )
        val orderQuantity=arrayListOf(
            "8",
            "85",
            "2"
        )
        val orderFoodImage=arrayListOf(
            R.drawable.menuphoto1,
            R.drawable.menuphoto3,
            R.drawable.menuphoto2
        )

        val adapter= PendingOrderAdapter(orderCustomerName,orderQuantity,orderFoodImage, this)
        binding.pendingOrderRecyclerview.adapter=adapter
        binding.pendingOrderRecyclerview.layoutManager= LinearLayoutManager(this)





    }
}
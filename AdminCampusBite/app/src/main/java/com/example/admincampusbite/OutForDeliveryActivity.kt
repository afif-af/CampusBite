package com.example.admincampusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.adapter.DeliveryAdapter
import com.example.admincampusbite.databinding.ActivityOutForDeliveryBinding
import com.example.admincampusbite.model.OrderDetails
import com.google.firebase.database.*

class OutForDeliveryActivity : AppCompatActivity() {

    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton3.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // retrieve and dispatch complete order
        retriveCompleteOrderDetail()
    }

    private fun retriveCompleteOrderDetail() {
        database = FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference
            .child("CompleteOrder")
            .orderByChild("currentTime")

        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear before populating data
                listOfCompleteOrderList.clear()

                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                }

                // reverse the list to display latest order first
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error properly
            }
        })
    }

    private fun setDataIntoRecyclerView() {
        // initialization list to hold customer name and payment status
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<String>()

        for (order in listOfCompleteOrderList) {
            order.userName?.let { customerName.add(it) }
            moneyStatus.add(
                if (order.paymentReceived == true) "Received" else "Not Received"
            )

        }

        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerView.adapter = adapter
    }
}

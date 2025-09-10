package com.example.admincampusbite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.adapter.PendingOrderAdapter
import com.example.admincampusbite.databinding.ActivityPendingOrderBinding
import com.example.admincampusbite.model.OrderDetails
import com.google.firebase.database.*

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked{
    private lateinit var binding: ActivityPendingOrderBinding
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirtFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialization of database
        database = FirebaseDatabase.getInstance()
        // initialize dataRef
        databaseOrderDetails = database.reference.child("OrderDetails")
        getOrdersDetails()

        binding.backButton5.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getOrdersDetails() {
        // retrieve orderDetails data from firebase
        databaseOrderDetails.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear old data first
                listOfOrderItem.clear()
                listOfName.clear()
                listOfTotalPrice.clear()
                listOfImageFirtFoodOrder.clear()

                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional: Handle error
            }
        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            // add to respective list for populating the recyclerview
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }

            // safely add first food image if available
            if (orderItem.foodImage.isNotEmpty()) {
                val firstImage = orderItem.foodImage[0]
                if (firstImage.isNotEmpty()) {
                    listOfImageFirtFoodOrder.add(firstImage)
                }
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRecyclerview.layoutManager = LinearLayoutManager(this)
        val adapter =
            PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImageFirtFoodOrder,this)
        binding.pendingOrderRecyclerview.adapter = adapter
    }

    override fun onItemClickListner(position: Int) {
        val intent= Intent(this,OrderDetailsActivity::class.java)
        val userOrderDetails= listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)

    }

    override fun onItemAcceptedClickListner(position: Int) {
         //handle item acceptance and update database
        val childItemPushKey=listOfOrderItem[position].itemPushKey
        val clickItemOrderReference=childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("AcceptedOrder")?.setValue(true)
       updateOrderAcceptStatus(position)

    }

    private fun updateOrderAcceptStatus(position: Int) {
        //update order accepteance in user BuyHisytory and OrderDetails
        val userIdOfClickedItem=listOfOrderItem[position].userUid
        val pushKeyOfClickedItem=listOfOrderItem[position].itemPushKey
        val userBuyHistoryReference=database.reference.child("users").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        userBuyHistoryReference.child("AcceptedOrder").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("AcceptedOrder").setValue(true)

    }

    override fun onItemDispatchClickListner(position: Int) {
        // handle item dispatch and updata database
        val dispatchItemPushKey=listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference=database.reference.child("CompleteOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsReference=database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Order is Dispatch",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this,"Order is not Dispatch",Toast.LENGTH_SHORT).show()

            }

    }

}


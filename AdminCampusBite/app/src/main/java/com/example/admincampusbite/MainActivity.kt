package com.example.admincampusbite

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincampusbite.databinding.ActivityMainBinding
import com.example.admincampusbite.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.addMenu.setOnClickListener {
            val intent= Intent(this, AddItemActivity::class.java)
            startActivity(intent)

        }
        binding.allItemMenu.setOnClickListener {
            val intent= Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.adminProfile.setOnClickListener {
            val intent= Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)

        }
        binding.createNewUser.setOnClickListener {
            val intent= Intent(this, CreateUserActivity::class.java)
            startActivity(intent)

        }
        binding.orderDispatch.setOnClickListener {
            val intent= Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)

        }
        binding.logOut.setOnClickListener {
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        binding.pendingOrderTextview.setOnClickListener {
            val intent= Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }


        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Sign out user
          //  auth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }

     pendingOrders()

     completeOrders()

     wholeTimeEarning()

    }
    private fun wholeTimeEarning() {
        val listOfTotalPay = mutableListOf<Int>()
        val completeOrderReference = FirebaseDatabase.getInstance().reference.child("CompleteOrder")

        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    val priceStr = completeOrder?.totalPrice ?: "0"
                    val cleanedPrice = priceStr.replace("[^\\d]".toRegex(), "")
                    val price = cleanedPrice.toIntOrNull() ?: 0
                    listOfTotalPay.add(price)
                }
                binding.totalEarning.text = "${listOfTotalPay.sum()}৳"
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional: Handle error
            }
        })
    }


    private fun completeOrders()
    {
        val completeOrderReference=database.reference.child("CompleteOrder")
        var completeOrderItemCount=0
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completeOrderItemCount=snapshot.childrenCount.toInt()
                binding.completedOrderItemCount.text=completeOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrders() {
        database= FirebaseDatabase.getInstance()

        val pendingOrderReference=database.reference.child("OrderDetails")
        var pendingOrderItemCount=0

        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                pendingOrderItemCount=snapshot.childrenCount.toInt()
                binding.textViewPendingCount.text=pendingOrderItemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

}
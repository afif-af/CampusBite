package com.example.campusbite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusbite.databinding.ActivityPayOutBinding
import com.example.campusbite.model.CartItems
import com.example.campusbite.model.OrderHistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PayOutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var cartList: MutableList<CartItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        cartList = mutableListOf()

        loadCartAndCalculateTotal()
        loadUserDetails()

        binding.button5.setOnClickListener { finish() }

        binding.placeMyOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun loadCartAndCalculateTotal() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = database.child("cart").child(userId)

        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                var total = 0

                for (itemSnapshot in snapshot.children) {
                    val cartItem = itemSnapshot.getValue(CartItems::class.java)
                    cartItem?.let {
                        cartList.add(it)
                        val price = it.foodPrice?.toIntOrNull() ?: 0
                        val quantity = it.foodQuantity?.toIntOrNull() ?: 1
                        total += price * quantity
                    }
                }

                binding.editTextText8.setText(total.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PayOutActivity, "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadUserDetails() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = database.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java) ?: ""
                val address = snapshot.child("address").getValue(String::class.java) ?: ""
                val phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                binding.editTextText4.setText(name)
                binding.editTextText5.setText(address)
                binding.editTextText6.setText(phone)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun placeOrder() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = database.child("cart").child(userId)

        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartItems>()
                var total = 0

                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(CartItems::class.java)
                    item?.let {
                        cartItems.add(it)
                        val price = it.foodPrice?.toIntOrNull() ?: 0
                        val qty = it.foodQuantity?.toIntOrNull() ?: 1
                        total += price * qty
                    }
                }

                // Save order info
                val ordersRef = database.child("orders").child(userId).push()
                val orderId = ordersRef.key ?: ""

                // Save order items as history list
                val orderHistoryList = cartItems.map {
                    OrderHistoryItem(
                        orderId = orderId,
                        foodName = it.foodName,
                        foodPrice = it.foodPrice,
                        foodImage = it.foodImage,
                        foodQuantity = it.foodQuantity,
                        orderTime = System.currentTimeMillis()
                    )
                }

                // Save order metadata
                val orderMap = hashMapOf<String, Any>(
                    "name" to binding.editTextText4.text.toString(),
                    "address" to binding.editTextText5.text.toString(),
                    "phone" to binding.editTextText6.text.toString(),
                    "total" to total.toString(),
                    "orderTime" to System.currentTimeMillis()
                )

                // Save order metadata
                ordersRef.setValue(orderMap).addOnSuccessListener {

                    // Save order items as history under "order_history"
                    val historyRef = database.child("order_history").child(userId).child(orderId)

                    val historyMap = orderHistoryList.mapIndexed { index, orderHistoryItem ->
                        index.toString() to orderHistoryItem
                    }.toMap()

                    historyRef.setValue(historyMap).addOnSuccessListener {

                        // Clear cart after order placed
                        database.child("cart").child(userId).removeValue().addOnSuccessListener {
                            Toast.makeText(this@PayOutActivity, "Order placed successfully", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@PayOutActivity, MainActivity::class.java)
                            intent.putExtra("goToHistory", true)
                            startActivity(intent)
                            finish()
                        }

                    }.addOnFailureListener {
                        Toast.makeText(this@PayOutActivity, "Failed to save order history", Toast.LENGTH_SHORT).show()
                    }

                }.addOnFailureListener {
                    Toast.makeText(this@PayOutActivity, "Failed to place order", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}

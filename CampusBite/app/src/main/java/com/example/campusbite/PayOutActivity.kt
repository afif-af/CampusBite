package com.example.campusbite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusbite.databinding.ActivityPayOutBinding
import com.example.campusbite.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private var totalAmount: Int = 0
    private var foodItemName: ArrayList<String> = arrayListOf()
    private var foodItemPrice: ArrayList<String> = arrayListOf()
    private var foodItemImage: ArrayList<String> = arrayListOf()
    private var foodItemQuantity: ArrayList<Int> = arrayListOf()
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setUserData()

        foodItemName = intent.getStringArrayListExtra("foodItemName") ?: arrayListOf()
        foodItemPrice = intent.getStringArrayListExtra("foodItemPrice") ?: arrayListOf()
        foodItemQuantity = intent.getIntegerArrayListExtra("foodItemQuantity") ?: arrayListOf()
        foodItemImage = intent.getStringArrayListExtra("foodItemImage") ?: arrayListOf()

        totalAmount = calculateTotalAmount()
        binding.totalAmount.setText("$totalAmount ৳")

        binding.backButton.setOnClickListener { finish() }

        binding.placeMyOrder.setOnClickListener {
            name = binding.name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }
    }

    private fun calculateTotalAmount(): Int {
        var total = 0
        for (i in foodItemPrice.indices) {
            val priceStr = foodItemPrice[i].replace("[^0-9]".toRegex(), "")
            val priceInt = priceStr.toIntOrNull() ?: 0
            val qty = if (i < foodItemQuantity.size) foodItemQuantity[i] else 1
            total += priceInt * qty
        }
        return total
    }

    private fun setUserData() {
        val user = auth.currentUser ?: return
        userId = user.uid
        val userRef = database.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val names = snapshot.child("name").getValue(String::class.java) ?: ""
                    val addresses = snapshot.child("address").getValue(String::class.java) ?: ""
                    val phones = snapshot.child("phone").getValue(String::class.java) ?: ""
                    binding.name.setText(names)
                    binding.address.setText(addresses)
                    binding.phone.setText(phones)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PayOutActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun placeOrder() {
        val time = System.currentTimeMillis()
        val itemPushKey = database.child("OrderDetails").push().key ?: return
        val orderDetails = OrderDetails(
            userId = userId,
            name = name,
            foodItemName = foodItemName,
            foodItemPrice = foodItemPrice,
            foodItemImage = intent.getStringArrayListExtra("foodItemImage") ?: arrayListOf(),
            foodItemQuantities =foodItemQuantity,
            address = address,
            totalAmount = "$totalAmount ৳",
            phone = phone,
            time = time,
            itemPushKey = itemPushKey,
            orderAccepted = false,
            paymentReceived = false
        )

        database.child("OrderDetails").child(itemPushKey).setValue(orderDetails)
            .addOnSuccessListener {
                removeItemsFromCart()
                addOrderToHistory(orderDetails)
                Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                // ✅ Show BottomSheet
                val bottomSheet = CongratsBottomSheet()
                bottomSheet.show(supportFragmentManager, "CongratsBottomSheet")

            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()

            }
    }

    private fun removeItemsFromCart() {
        database.child("users").child(userId).child("CartItems").removeValue()
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        database.child("users").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!).setValue(orderDetails)
    }
}

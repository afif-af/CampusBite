package com.example.campusbite.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.PayOutActivity
import com.example.campusbite.adapter.CartAdapter
import com.example.campusbite.databinding.FragmentCartBinding
import com.example.campusbite.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var foodDescription: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var userId: String
    private lateinit var cartAdapter: CartAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        reteriveCartItems()

        binding.proceedButton.setOnClickListener {
            val updatedQuantities = cartAdapter.getUpdatedItemQuantities()
            getOrderItemsDetails(updatedQuantities)
        }

        return binding.root
    }

    private fun getOrderItemsDetails(foodQuantities: MutableList<Int>) {
        val orderIdReference: DatabaseReference = database.child("users").child(userId).child("CartItems")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodIngredients?.let { foodIngredient.add(it) }
                }
                orderNow(foodName, foodPrice, foodImage, foodDescription, foodIngredient, foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        foodDescription: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putStringArrayListExtra("foodItemName", ArrayList(foodName))
            intent.putStringArrayListExtra("foodItemPrice", ArrayList(foodPrice))
            intent.putStringArrayListExtra("foodItemImage", ArrayList(foodImage))
            intent.putStringArrayListExtra("foodItemDescription", ArrayList(foodDescription))
            intent.putStringArrayListExtra("foodItemIngredient", ArrayList(foodIngredient))
            intent.putIntegerArrayListExtra("foodItemQuantity", ArrayList(foodQuantities))
            startActivity(intent)
        }
    }

    private fun reteriveCartItems() {
        userId = auth.currentUser?.uid ?: ""
        val foodReference: DatabaseReference = database.child("users").child(userId).child("CartItems")

        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImagesUri = mutableListOf()
        foodDescription = mutableListOf()
        quantity = mutableListOf()
        foodIngredients = mutableListOf()

        foodReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                foodNames.clear()
                foodPrices.clear()
                foodImagesUri.clear()
                foodDescription.clear()
                quantity.clear()
                foodIngredients.clear()

                for (foodSnapshot in snapshot.children) {
                    val cartItem = foodSnapshot.getValue(CartItems::class.java)
                    cartItem?.let {
                        foodNames.add(it.foodName ?: "")
                        foodPrices.add(it.foodPrice ?: "")
                        foodImagesUri.add(it.foodImage ?: "")
                        foodDescription.add(it.foodDescription ?: "")
                        quantity.add(it.foodQuantity ?: 1)
                        foodIngredients.add(it.foodIngredients ?: "")
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun setAdapter() {
        if (!isAdded || context == null) return
        cartAdapter = CartAdapter(
            requireContext(),
            foodNames,
            foodPrices,
            foodImagesUri,
            foodDescription,
            quantity,
            foodIngredients
        )
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = cartAdapter
    }


}

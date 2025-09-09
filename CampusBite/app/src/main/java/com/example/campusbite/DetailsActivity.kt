package com.example.campusbite

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.campusbite.databinding.ActivityDetailsBinding
import com.example.campusbite.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredient: String? = null
    private var foodPrice: String? = null


    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        // Intent থেকে data নিচ্ছে
        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        // View-এ সেট করা
        binding.apply {
            detailsFoodName.text = foodName
            descriptionTextView.text = foodDescription
            ingredientTextView.text = foodIngredient

            // Glide দিয়ে ইমেজ লোড
            Glide.with(this@DetailsActivity)
                .load(Uri.parse(foodImage))
                .into(detailsFoodImage)
        }

        // Back বাটনের ক্লিক লিসেনার
        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.atToCartButton.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {

        //val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?:"" //return
        val database = FirebaseDatabase.getInstance().reference


//        val cartItem = CartItems(
//            foodName = foodName,
//            foodPrice = foodPrice,
//            foodDescription = foodDescription,
//            foodImage = foodImage,
//            foodQuantity = "1"
//        )
        val cartItem= CartItems(foodName.toString(), foodPrice.toString(),foodDescription.toString(),foodImage.toString(), 1 )

        database.child("users").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to add item: ${it.message}", Toast.LENGTH_SHORT).show()

        }
}




//        val itemId = cartRef.push().key ?: return
//
//        cartRef.child(itemId).setValue(cartItem).addOnSuccessListener {
//            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener {
//            Toast.makeText(this, "Failed to add item: ${it.message}", Toast.LENGTH_SHORT).show()
//        }
}

//    private fun addItemToCart() {
//        val database: FirebaseDatabase= FirebaseDatabase.getInstance().reference
//        val userId:String=auth.currentUser?.uid?:""
//
//    }
//    //Create c cartIenms object
//    val cartItems= CartItems(foodImage.toString(),
//        foodPrice.toString(),
//        foodDescription.toString(),
//        foodImage.toString(),
//        1)
//    {
//
//        // save data to cart item to firebase
//
//    }


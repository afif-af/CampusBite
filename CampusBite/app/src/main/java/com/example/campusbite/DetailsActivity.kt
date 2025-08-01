package com.example.campusbite

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.campusbite.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredient: String? = null
    private var foodPrice: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

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
    }
}

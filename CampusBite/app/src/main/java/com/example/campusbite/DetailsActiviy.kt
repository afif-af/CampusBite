package com.example.campusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campusbite.databinding.ActivityDetailsBinding

class DetailsActiviy : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        val foodName=intent.getStringExtra("MenuItemName")
        val foodImage=intent.getIntExtra("MenuItemImage",0)
        binding.detailsFoodName.text=foodName
        binding.detailsFoodImage.setImageResource(foodImage)

        binding.imageButton.setOnClickListener {
            finish()
        }


    }

}
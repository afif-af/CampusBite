package com.example.campusbite

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campusbite.databinding.ActivityChooseYourLocationBinding

class ChooseYourLocationActivity : AppCompatActivity() {
    private  val binding: ActivityChooseYourLocationBinding by lazy {
        ActivityChooseYourLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val locationList=arrayOf("Mymensingh","Chandpur","Dhaka","Chattraogram")
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView =binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.description.setOnClickListener {
           startActivity (Intent(this, MainActivity::class.java))
        }

    }
}
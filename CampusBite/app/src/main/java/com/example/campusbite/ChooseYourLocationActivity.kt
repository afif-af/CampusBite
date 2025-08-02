package com.example.campusbite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.campusbite.databinding.ActivityChooseYourLocationBinding

class ChooseYourLocationActivity : AppCompatActivity() {
    private val binding: ActivityChooseYourLocationBinding by lazy {
        ActivityChooseYourLocationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.listOfLocation.setOnClickListener {
            val intent = Intent(this, MapPickerActivity::class.java)
            startActivityForResult(intent, 1001)
        }

        binding.description.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
        val savedLocation = prefs.getString("user_location", null)
        savedLocation?.let {
            binding.listOfLocation.setText(it)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val selectedLocation = data?.getStringExtra("location")
            binding.listOfLocation.setText(selectedLocation)

            // ✅ Save to SharedPreferences
            val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
            prefs.edit().putString("user_location", selectedLocation).apply()
        }
    }



}

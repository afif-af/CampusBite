package com.example.admincampusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.adapter.AddItemAdapter
import com.example.admincampusbite.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {
    private  val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Sample data (replace with your actual data source, e.g., from a database)
        val menuNames = mutableListOf("Burger", "Pizza", "Salad", "Pasta", "Sushi")
        val menuPrices = mutableListOf("$10", "$15", "$8", "$12", "$20")
        val menuImages = mutableListOf(
            R.drawable.menuphoto1,
            R.drawable.menuphoto2,
            R.drawable.menuphoto3,
            R.drawable.menuphoto,
            R.drawable.menuphoto4) // Use your actual drawable resources

        val adapter = AddItemAdapter(menuNames, menuPrices, menuImages)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter = adapter

        binding.backButton2.setOnClickListener {
            finish() // Go back to the previous activity
        }

    }

}
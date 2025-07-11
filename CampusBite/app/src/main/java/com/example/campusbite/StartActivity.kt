package com.example.campusbite

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campusbite.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private val binding: ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // ✅ Fixed typo: OnclickListern → setOnClickListener
        binding.nextButton.setOnClickListener {
            // ✅ Corrected Intent syntax: this → this@StartActivity, LoginActivity::class.java
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_start)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
}
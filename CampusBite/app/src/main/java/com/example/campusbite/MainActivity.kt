package com.example.campusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.campusbite.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var NavController=findNavController(R.id.fragmentContainerView4)
        var bottomnav=findViewById<BottomNavigationView>(R.id.bottomNavigationView2)
        bottomnav.setupWithNavController(NavController)

        binding.notificationButton.setOnClickListener {
            val bottomSheetDialog = NotificationBottomFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")

        }



    }
    // In MainActivity.kt
//    override fun onResume() {
//        super.onResume()
//        val goToHistory = intent.getBooleanExtra("goToHistory", false)
//        if (goToHistory) {
//            bottomNavigationView.selectedItemId = R.id.historyFragment
//        }
//    }


}
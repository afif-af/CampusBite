package com.example.admincampusbite

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincampusbite.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.addMenu.setOnClickListener {
            val intent= Intent(this, AddItemActivity::class.java)
            startActivity(intent)

        }
        binding.allItemMenu.setOnClickListener {
            val intent= Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }

        binding.adminProfile.setOnClickListener {
            val intent= Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)

        }
        binding.createNewUser.setOnClickListener {
            val intent= Intent(this, CreateUserActivity::class.java)
            startActivity(intent)

        }
        binding.orderDispatch.setOnClickListener {
            val intent= Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)

        }
        binding.logOut.setOnClickListener {
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        binding.pendingOrderTextview.setOnClickListener {
            val intent= Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }


        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Sign out user

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }

    }
}
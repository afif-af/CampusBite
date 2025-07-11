package com.example.admincampusbite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincampusbite.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.etName.isEnabled=false
        binding.etAddress.isEnabled=false
        binding.etPhone.isEnabled=false
        binding.etPassword.isEnabled=false

        var isEnable=false
        binding.editProfile.setOnClickListener {
            isEnable =! isEnable
            binding.etName.isEnabled=isEnable
            binding.etAddress.isEnabled=isEnable
            binding.etPhone.isEnabled=isEnable
            binding.etPassword.isEnabled=isEnable
            if(isEnable)
            {
                binding.etName.requestFocus()
            }

        }

    }









}
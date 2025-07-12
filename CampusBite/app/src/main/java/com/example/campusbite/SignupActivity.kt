package com.example.campusbite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campusbite.databinding.ActivitySignupBinding
import com.example.campusbite.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Initialize Firebase Auth

        auth = FirebaseAuth.getInstance()

//        auth=Firebase.Auth

        database = FirebaseDatabase.getInstance().reference




        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        



        binding.createAccountButton.setOnClickListener {
            userName = binding.userName.text.toString() //,trim() delete korlam
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }
    }

    private fun createAccount(email: String,password:String)
    {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                task -> if(task.isSuccessful)
        {
            Toast.makeText(this,"Account Create Sucessfully",Toast.LENGTH_SHORT).show()
            saveUserData()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        else
            {
                Toast.makeText(this,"Account create fialed",Toast.LENGTH_SHORT).show()
                Log.d("Account","CreateAccount: Failure",task.exception)

            }
        }

    }

    private fun saveUserData() {

        //retrive data from input filed
        userName=binding.userName.text.toString()
        email=binding.email.text.toString().trim()
        password=binding.password.text.toString().trim()

        val user= UserModel(userName,email,password)
        val userId= FirebaseAuth.getInstance().currentUser!!.uid

        database.child("user").child(userId).setValue(user)

//        val userId = auth.currentUser?.uid
//        val user = mapOf(
//            "userName" to userName,
//            "email" to email
//        )
//        if (userId != null) {
//            FirebaseDatabase.getInstance().getReference("Users")
//                .child(userId)
//                .setValue(user)
//                .addOnSuccessListener {
//                    Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
//                }
//        }
    }

}
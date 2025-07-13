package com.example.campusbite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.campusbite.databinding.ActivitySignupBinding
import com.example.campusbite.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignupActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var email: String
    private lateinit var password: String

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(binding.root)

        // initianle firebase
        auth = Firebase.auth
        // initalize firebase database
        database = Firebase.database.reference



        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        binding.createAccountButton.setOnClickListener {
            //get text from edit text
            userName = binding.userName.text.toString().trim()

            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(userName.isBlank() || email.isBlank()||password.isBlank())
            {
                Toast.makeText(this,"please fill all details", Toast.LENGTH_SHORT).show()
            }else
            {
                createAccount(email,password)
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

    //save data into databse
    private fun saveUserData()
    {
        userName = binding.userName.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user= UserModel(userName,email,password)
        val userId= FirebaseAuth.getInstance().currentUser!!.uid

        //save data user in fire base
        database.child("user").child(userId).setValue(user)


    }
}
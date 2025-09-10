package com.example.admincampusbite

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincampusbite.databinding.ActivityAdminProfileBinding
import com.example.admincampusbite.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.Reference
import java.security.acl.Owner

class AdminProfileActivity : AppCompatActivity() {
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("userAdmin")


        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.saveInfoButton.setOnClickListener {
            updateUserData()
        }

        binding.etName.isEnabled = false
        binding.etAddress.isEnabled = false
        binding.etPhone.isEnabled = false
        binding.etPassword.isEnabled = false
        binding.saveInfoButton.isEnabled = false


        var isEnable = false
        binding.editProfile.setOnClickListener {
            isEnable = !isEnable
            binding.etName.isEnabled = isEnable
            binding.etAddress.isEnabled = isEnable
            binding.etPhone.isEnabled = isEnable
            binding.etPassword.isEnabled = isEnable
            if (isEnable) {
                binding.etName.requestFocus()
            }
            binding.saveInfoButton.isEnabled=isEnable

        }
        retriveUserData()

    }

    private fun retriveUserData() {
        val currentUserUid=auth.currentUser?.uid
        if(currentUserUid!=null)
        {
            val userReference=adminReference.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ownerName = snapshot.child("name").getValue()
                        var email = snapshot.child("email").getValue()
                        var password = snapshot.child("password").getValue()
                        var address = snapshot.child("address").getValue()
                        var phone = snapshot.child("phone").getValue()
                        setDataToTextView(ownerName, email, password,  phone,address)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

    }
    private fun setDataToTextView(
        ownerName: Any?,
        email:Any?,
        password:Any?,
        phone:Any?,
        address:Any?
    ){
        binding.etName.setText(ownerName.toString())
        binding.etEmail.setText(email.toString())
        binding.etPassword.setText(password.toString())
        binding.etPhone.setText(phone.toString())
        binding.etAddress.setText(address.toString())

    }
    private fun updateUserData()
    {
        var updateName=binding.etName.text.toString()
        var updateEmail=binding.etEmail.text.toString()
        var updatePassword=binding.etPassword.text.toString()
        var updatePhone=binding.etPhone.text.toString()
        var updateAddress=binding.etAddress.text.toString()
        val currentUserUid=auth.currentUser?.uid
        if(currentUserUid!=null)
        {
            val userReference=adminReference.child(currentUserUid)

            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)

            Toast.makeText(this,"Profile updated success full", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)


        }

        else {
            Toast.makeText(this, "Profile updated failed", Toast.LENGTH_SHORT).show()

        }

    }


}
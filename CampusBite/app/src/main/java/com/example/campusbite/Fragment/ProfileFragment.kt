package com.example.campusbite.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campusbite.LoginActivity
import com.example.campusbite.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserData()

        binding.button3.setOnClickListener {
            saveUserData()
        }

        binding.logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = database.child("users").child(userId)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java)
                val address = snapshot.child("address").getValue(String::class.java)
                val email = snapshot.child("email").getValue(String::class.java)
                val phone = snapshot.child("phone").getValue(String::class.java)

                binding.etName.setText(name)
                binding.etAddress.setText(address)
                binding.etEmail.setText(email)
                binding.etPhone.setText(phone)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = database.child("users").child(userId)

        val name = binding.etName.text.toString()
        val address = binding.etAddress.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhone.text.toString()

        val userMap = mapOf(
            "name" to name,
            "address" to address,
            "email" to email,
            "phone" to phone
        )

        userRef.setValue(userMap).addOnSuccessListener {
            Toast.makeText(requireContext(), "Information saved", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}



//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import com.example.campusbite.LoginActivity
//import com.example.campusbite.R
//import com.google.firebase.auth.FirebaseAuth
//
//class ProfileFragment : Fragment() {
//
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        auth = FirebaseAuth.getInstance()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_profile, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val logoutButton = view.findViewById<Button>(R.id.logout)
//        logoutButton.setOnClickListener {
//            auth.signOut()
//
//            val intent = Intent(requireContext(), LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }
//    }
//}

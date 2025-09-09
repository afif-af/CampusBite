package com.example.campusbite.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.campusbite.LoginActivity
import com.example.campusbite.databinding.FragmentProfileBinding
import com.example.campusbite.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set user data on fragment load
        setUserData()

        binding.apply {
            // Disable fields initially
            etName.isEnabled = false
            etEmail.isEnabled = false
            etAddress.isEnabled = false
            etPhone.isEnabled = false

            // Edit button click → toggle enable state
            editButton.setOnClickListener {
                etName.isEnabled = !etName.isEnabled
                etEmail.isEnabled = !etEmail.isEnabled
                etAddress.isEnabled = !etAddress.isEnabled
                etPhone.isEnabled = !etPhone.isEnabled
            }
        }

        // Save/Update button click
        binding.saveInformation.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                updateUserData(name, email, address, phone)
            }
        }

        // Logout button click
        binding.logout.setOnClickListener {
            logoutUser()
        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)

            val userData = hashMapOf(
                "name" to name,
                "email" to email,
                "address" to address,
                "phone" to phone
            )

            userReference.setValue(userData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userReference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userProfile = snapshot.getValue(UserModel::class.java)
                    userProfile?.let {
                        binding.etName.setText(it.name)
                        binding.etEmail.setText(it.email)
                        binding.etAddress.setText(it.address)
                        binding.etPhone.setText(it.phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun logoutUser() {
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}

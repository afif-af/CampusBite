package com.example.admincampusbite

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.admincampusbite.databinding.ActivityAddItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.admincampusbite.model.CloudinaryUploader
import org.json.JSONObject
import java.io.File
import com.example.admincampusbite.util.FileUtils


class AddItemActivity : AppCompatActivity() {

    private lateinit var foodName: String
    private  lateinit var foodImage: String
    private  lateinit var foodPrice: String
    private  lateinit var foodDescription: String
    private  lateinit var foodIngredient: String
    private  var foodImageUri: Uri ?= null

    //fireBase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase



    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialize firebase
        auth= FirebaseAuth.getInstance()
        //firebase database instance
        database= FirebaseDatabase.getInstance()

        binding.atItemButton.setOnClickListener {
            //get data from filed
            foodName=binding.foodName.text.toString().trim()
            foodPrice=binding.foodPrice.text.toString().trim()
            foodDescription=binding.foodDescription.text.toString().trim()
            foodIngredient=binding.foodIngredient.text.toString().trim()


            if( foodName.isBlank()|| foodPrice.isBlank()||foodDescription.isBlank()||foodIngredient.isBlank())
            {
                uploadData()
                Toast.makeText(this,"Item Add Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            else
            {
                Toast.makeText(this,"Fill all details ", Toast.LENGTH_SHORT).show()

            }


        }
        binding.selectedImage.setOnClickListener {
            pickImage.launch("image/*")
        }


        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun uploadData() {
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        if (foodImageUri == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val file = FileUtils.getFile(this, foodImageUri!!)
        if (file == null) {
            Toast.makeText(this, "Failed to get image file", Toast.LENGTH_SHORT).show()
            return
        }

        CloudinaryUploader.uploadImage(file) { imageUrl ->
            if (imageUrl != null) {
                val newItem = hashMapOf(
                    "name" to foodName,
                    "price" to foodPrice,
                    "description" to foodDescription,
                    "ingredient" to foodIngredient,
                    "imageUrl" to imageUrl
                )

                if (newItemKey != null) {
                    menuRef.child(newItemKey).setValue(newItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload data", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
        }
    }
}


package com.example.admincampusbite

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincampusbite.adapter.MenuItemAdapter
import com.example.admincampusbite.databinding.ActivityAllItemBinding
import com.example.admincampusbite.model.AllMenu
import com.google.firebase.database.*

class AllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItem: ArrayList<AllMenu> = ArrayList()

    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        retrieveMenuItem()

        binding.backButton2.setOnClickListener {
            onBackPressed()
        }
    }

    private fun retrieveMenuItem() {
        val foodRef = database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItem.clear()

                for (foodSnapshot in snapshot.children) {
                    val item = foodSnapshot.getValue(AllMenu::class.java)
                    item?.let {
                        menuItem.add(it)
                    }
                }

                Log.d("Firebase", "Items found: ${menuItem.size}")

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Database Error", "${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemActivity, menuItem)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter = adapter
    }
}

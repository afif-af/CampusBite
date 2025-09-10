package com.example.admincampusbite

import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
    private lateinit var binding: ActivityAllItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("menu")

        retrieveMenuItem()

        binding.backButton2.setOnClickListener {
            onBackPressed()
        }
    }

    private fun retrieveMenuItem() {
        val foodRef = databaseReference

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItem.clear()

                for (foodSnapshot in snapshot.children) {
                    val item = foodSnapshot.getValue(AllMenu::class.java)
                    // attach the node key into the model
                    val itemWithKey = item?.copy(key = foodSnapshot.key)
                    itemWithKey?.let {
                        menuItem.add(it)
                    }
                }

                Log.d("Firebase", "Items found: ${menuItem.size}")
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Database Error", "${error.message}")
                Toast.makeText(this@AllItemActivity, "Failed to load items", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@AllItemActivity, menuItem, databaseReference) { position ->
            deleteMenuItems(position)
        }
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuRecyclerView.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        if (position < 0 || position >= menuItem.size) {
            Toast.makeText(this, "Invalid position", Toast.LENGTH_SHORT).show()
            return
        }

        val menuItemToDelete = menuItem[position]
        val menuItemKey = menuItemToDelete.key

        if (menuItemKey == null) {
            Toast.makeText(this, "Cannot delete: key is null", Toast.LENGTH_SHORT).show()
            return
        }

        val foodMenuReference = databaseReference.child(menuItemKey)
        foodMenuReference.removeValue()
            .addOnSuccessListener {
                menuItem.removeAt(position)
                binding.menuRecyclerView.adapter?.notifyItemRemoved(position)
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Item not deleted", Toast.LENGTH_SHORT).show()
            }
    }
}

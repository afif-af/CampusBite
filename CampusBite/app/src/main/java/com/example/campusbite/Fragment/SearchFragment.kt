package com.example.campusbite.Fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.adapter.MenuAdapter
import com.example.campusbite.databinding.FragmentSearchBinding
import com.example.campusbite.model.MenuItem
import com.google.firebase.database.*

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var databaseReference: DatabaseReference

    private val allMenuItems = mutableListOf<MenuItem>()
    private val filteredMenuItems = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Firebase DB reference
        databaseReference = FirebaseDatabase.getInstance().getReference("menu")

        // RecyclerView setup
        adapter = MenuAdapter(filteredMenuItems, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        setupSearchView()
        fetchMenuItemsFromFirebase()

        return binding.root
    }

    private fun fetchMenuItemsFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                allMenuItems.clear()
                for (data in snapshot.children) {
                    val item = data.getValue(MenuItem::class.java)
                    item?.let { allMenuItems.add(it) }
                }

                // Show all items initially
                filteredMenuItems.clear()
                filteredMenuItems.addAll(allMenuItems)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: handle error e.g. show Toast or Log
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocusFromTouch()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenuItems(newText)
                return true
            }
        })

        val searchAutoComplete = binding.searchView.findViewById<AutoCompleteTextView>(
            androidx.appcompat.R.id.search_src_text
        )
        searchAutoComplete.hint = "What do you want to order?"
        searchAutoComplete.setHintTextColor(Color.GRAY)
        searchAutoComplete.setTextColor(Color.BLACK)
    }

    private fun filterMenuItems(query: String?) {
        filteredMenuItems.clear()
        if (query.isNullOrBlank()) {
            filteredMenuItems.addAll(allMenuItems)
        } else {
            val lowerQuery = query.trim().lowercase()
            filteredMenuItems.addAll(allMenuItems.filter {
                it.name?.lowercase()?.contains(lowerQuery) == true
            })
        }
        adapter.notifyDataSetChanged()
    }
}

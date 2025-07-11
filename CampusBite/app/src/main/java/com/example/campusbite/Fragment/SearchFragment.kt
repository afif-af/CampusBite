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
import com.example.campusbite.R
import com.example.campusbite.adapter.MenuAdapter
import com.example.campusbite.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter

    private val originalMenuFoodName = listOf(
        "Burger", "Sandwich", "Samosa",
        "Biryani", "Sandwich", "Item", "Burger",
        "Sandwich", "Samosa", "Biryani",
        "Sandwich", "Item"
    )
    private val originalMenuItemPrice = listOf(
        "10", "100", "20", "200", "30", "300",
        "10", "100", "20", "200", "30", "300"
    )
    private val originalImage = listOf(
        R.drawable.menuphoto1,
        R.drawable.menuphoto2,
        R.drawable.menuphoto3,
        R.drawable.menuphoto4,
        R.drawable.menuphoto1,
        R.drawable.menuphoto2,
        R.drawable.menuphoto1,
        R.drawable.menuphoto2,
        R.drawable.menuphoto3,
        R.drawable.menuphoto4,
        R.drawable.menuphoto1,
        R.drawable.menuphoto2
    )

    private val filteredMenuFoodName = mutableListOf<String>()
    private val filteredMenuItemPrice = mutableListOf<String>()
    private val filteredMenuImage = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        adapter = MenuAdapter(filteredMenuFoodName, filteredMenuItemPrice, filteredMenuImage,requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        setupSearchView()
        showAllMenu()

        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        filteredMenuFoodName.addAll(originalMenuFoodName)
        filteredMenuItemPrice.addAll(originalMenuItemPrice)
        filteredMenuImage.addAll(originalImage)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        // Make sure searchView is expanded and clickable
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()  // Prevent auto keyboard open initially

        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
            binding.searchView.requestFocusFromTouch()
        }

        // Set query text listener
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

        // Force query hint and text color setup
        val searchAutoComplete = binding.searchView.findViewById<AutoCompleteTextView>(
            androidx.appcompat.R.id.search_src_text
        )
        searchAutoComplete.hint = "What do you want to order?"
        searchAutoComplete.setHintTextColor(Color.GRAY)
        searchAutoComplete.setTextColor(Color.BLACK)
    }

    private fun filterMenuItems(query: String?) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        originalMenuFoodName.forEachIndexed { index, foodName ->
            if (foodName.contains(query.toString(), ignoreCase = true)) {
                filteredMenuFoodName.add(foodName)
                filteredMenuItemPrice.add(originalMenuItemPrice[index])
                filteredMenuImage.add(originalImage[index])
            }
        }

        adapter.notifyDataSetChanged()
    }
}

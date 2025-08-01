package com.example.campusbite.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.campusbite.MenuBottomSheetFragment
import com.example.campusbite.R
import com.example.campusbite.adapter.MenuAdapter
import com.example.campusbite.databinding.FragmentHomeBinding
import com.example.campusbite.model.MenuItem
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    private lateinit var subsetMenuItems: List<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance()
        menuItems = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Show bottom sheet for full menu
        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")
        }

        retrieveAndDisplayPopularItems()

        return binding.root
    }

    private fun retrieveAndDisplayPopularItems() {
        val foodRef: DatabaseReference = database.reference.child("menu")

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()

                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }

                if (menuItems.size < 6) {
                    Toast.makeText(requireContext(), "Not enough items in menu", Toast.LENGTH_SHORT).show()
                } else {
                    randomPopularItem()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun randomPopularItem() {
        val shuffledIndices: List<Int> = menuItems.indices.shuffled()
        val menuItemToShow = 6
        subsetMenuItems = shuffledIndices.take(menuItemToShow).map { menuItems[it] }

        setPopularItemAdapter()
    }

    private fun setPopularItemAdapter() {
        val adapter = MenuAdapter(subsetMenuItems.toMutableList(), requireContext())
        binding.PopulerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopulerRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up image slider
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // Optional double-click action
            }

            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

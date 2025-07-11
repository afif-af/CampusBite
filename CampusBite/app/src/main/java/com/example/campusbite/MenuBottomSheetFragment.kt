package com.example.campusbite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campusbite.adapter.MenuAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MenuBottomSheetFragment : BottomSheetDialogFragment() {
  private lateinit var binding: FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMenuBottomSheetBinding.inflate(inflater,container,false)

        binding.buttonBack.setOnClickListener {
            dismiss()
        }
        val menuFoodName = listOf("Burger", "Sandwich", "Samosa", "Biryani", "Sandwich", "Item","Burger", "Sandwich", "Samosa", "Biryani", "Sandwich", "Item")
        val menuItemPrice = listOf("10", "100", "20", "200", "30", "300","10", "100", "20", "200", "30", "300")
        val menuImage = listOf(
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

        val adapter = MenuAdapter(
            ArrayList(menuFoodName),
            ArrayList(menuItemPrice),
            ArrayList(menuImage),
            requireContext()
        )
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter



        return binding.root
    }

    companion object {

    }
}
package com.example.campusbite.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.CongratsBottomSheet
import com.example.campusbite.PayOutActivity
import com.example.campusbite.R
import com.example.campusbite.adapter.CartAdapter
import com.example.campusbite.databinding.CartItemBinding
import com.example.campusbite.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val cartFoodName = listOf("Burger", "Sandwich", "Samosa", "Biryani", "Sandwich", "Item")
        val cartItemPrice = listOf("10", "100", "20", "200", "30", "300")
        val cartImage = listOf(
            R.drawable.menuphoto1,
            R.drawable.menuphoto2,
            R.drawable.menuphoto3,
            R.drawable.menuphoto4,
            R.drawable.menuphoto1,
            R.drawable.menuphoto2
        )

        val adapter = CartAdapter(
            ArrayList(cartFoodName),
            ArrayList(cartItemPrice),
            ArrayList(cartImage)
        )

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        binding.proceedButton.setOnClickListener {
            val intent= Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    companion object {

    }
}
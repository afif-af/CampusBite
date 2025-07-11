package com.example.campusbite.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.R
import com.example.campusbite.adapter.BuyAgainAdapter
import com.example.campusbite.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {
  private lateinit var binding: FragmentHistoryBinding
  private lateinit var buyAgainAdapter: BuyAgainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHistoryBinding.inflate(layoutInflater,container,false)
        setupRecyclerView()
        return binding.root
    }
    private fun setupRecyclerView()
    {
        val buyAgainFoodName=arrayListOf("Food 1","Food 2","Food 3","Food 1","Food 2","Food 3","Food 1","Food 2","Food 3","Food 1","Food 2","Food 3","Food 1","Food 2","Food 3",)
        val buyAgainFoodPrice=arrayListOf("10","100","1000","10","100","1000","10","100","1000","10","100","1000","10","100","1000",)
        val buyAgainFoodImage=arrayListOf(
            R.drawable.menuphoto1,
            R.drawable.menuphoto2,
            R.drawable.menuphoto3,
            R.drawable.menuphoto4,
            R.drawable.menuphoto1,
            R.drawable.menuphoto2,
            R.drawable.menuphoto3,
            R.drawable.menuphoto4,
            R.drawable.menuphoto1,
            R.drawable.menuphoto2,
            R.drawable.menuphoto3,
            R.drawable.menuphoto4,
            R.drawable.menuphoto1,
            R.drawable.menuphoto2,
            R.drawable.menuphoto3)
        buyAgainAdapter= BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice, buyAgainFoodImage )
        binding.buyAgainRecyclerView.adapter=buyAgainAdapter
        binding.buyAgainRecyclerView.layoutManager= LinearLayoutManager(requireContext())


    }
    companion object {

    }
}
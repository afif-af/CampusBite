package com.example.campusbite.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.adapter.HistoryAdapter
import com.example.campusbite.databinding.FragmentHistoryBinding
import com.example.campusbite.model.HistoryItem
import com.example.campusbite.model.OrderDetails
import com.example.campusbite.model.OrderDetailsFirebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var historyAdapter: HistoryAdapter
    private var historyList: MutableList<HistoryItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()
        retrieveHistory()

        return binding.root
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(requireContext(), historyList)
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = historyAdapter
    }

    private fun retrieveHistory() {
        val userId = auth.currentUser?.uid ?: return
        val historyRef = database.child("users").child(userId).child("BuyHistory")

        historyRef.orderByChild("currentTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyList.clear()

                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderDetailsFirebase::class.java) // only here

                    if (order != null) {
                        val foodNames = order.foodName ?: arrayListOf()
                        val foodPrices = order.foodPrice ?: arrayListOf()
                        val foodImages = order.foodImage ?: arrayListOf()

                        for (i in foodNames.indices) {
                            val item = HistoryItem(
                                foodName = foodNames[i],
                                foodPrice = if (i < foodPrices.size) foodPrices[i] else "",
                                foodImage = if (i < foodImages.size) foodImages[i] else ""
                            )
                            historyList.add(item)
                        }
                    }
                }

                historyList.reverse()
                historyAdapter.updateList(historyList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load history", Toast.LENGTH_SHORT).show()
            }
        })
    }



}

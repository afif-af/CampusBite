package com.example.campusbite.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.adapter.BuyAgainAdapter
import com.example.campusbite.databinding.FragmentHistoryBinding
import com.example.campusbite.model.HistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var historyList = mutableListOf<HistoryItem>()
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()
        loadHistoryItems()

        return binding.root
    }

    private fun setupRecyclerView() {
        buyAgainAdapter = BuyAgainAdapter(historyList)
        binding.buyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.buyAgainRecyclerView.adapter = buyAgainAdapter
    }

    private fun loadHistoryItems() {
        val userId = auth.currentUser?.uid ?: return
        val historyRef = database.child("orders").child(userId)

        historyRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyList.clear()
                for (orderSnapshot in snapshot.children) {
                    // ধরছি orders এর মধ্যে items নামে আরেকটি লিস্ট আছে
                    val itemsSnapshot = orderSnapshot.child("items")
                    for (itemSnapshot in itemsSnapshot.children) {
                        val foodName = itemSnapshot.child("foodName").getValue(String::class.java) ?: ""
                        val foodPrice = itemSnapshot.child("foodPrice").getValue(String::class.java) ?: ""
                        val foodImageUrl = itemSnapshot.child("foodImage").getValue(String::class.java) ?: ""

                        val historyItem = HistoryItem(foodName, foodPrice, foodImageUrl)
                        historyList.add(historyItem)
                    }
                }
                buyAgainAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }
}

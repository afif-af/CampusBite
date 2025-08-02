package com.example.campusbite.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusbite.PayOutActivity
import com.example.campusbite.adapter.CartAdapter
import com.example.campusbite.adapter.OnCartUpdatedListener
import com.example.campusbite.databinding.FragmentCartBinding
import com.example.campusbite.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartList: MutableList<CartItems>
    private lateinit var adapter: CartAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        cartList = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadCartItems()

        binding.proceedButton.setOnClickListener {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun loadCartItems() {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = database.child("cart").child(userId)

        cartRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                for (itemSnapshot in snapshot.children) {
                    val cartItem = itemSnapshot.getValue(CartItems::class.java)
                    cartItem?.let { cartList.add(it) }
                }

                adapter = CartAdapter(cartList, object : OnCartUpdatedListener {
                    override fun onCartUpdated() {
                        // You can update UI if needed here
                    }
                })
                binding.cartRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load cart", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
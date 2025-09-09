package com.example.campusbite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusbite.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val cartImages: MutableList<String>,
    private val cartDescriptions: MutableList<String>,
    private val cartQuantity: MutableList<Int>,
    private val cartIngredient: MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val userId = auth.currentUser?.uid ?: ""

    companion object {
        private lateinit var itemQuantities: IntArray
        private lateinit var cartItemsReference: DatabaseReference
    }

    init {
        val cartItemNumber = cartItems.size
        itemQuantities = IntArray(cartItemNumber) { cartQuantity[it] } // init from db
        cartItemsReference = database.child("users").child(userId).child("CartItems")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    fun getUpdatedItemQuantities(): MutableList<Int> {
        val updatedQuantities = mutableListOf<Int>()
        updatedQuantities.addAll(cartQuantity)
        return updatedQuantities
    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = cartItemPrices[position]
                cartItemQuantity.text = cartQuantity[position].toString()

                Glide.with(context)
                    .load(cartImages[position])
                    .into(cartImage)

                plusButton.setOnClickListener { increaseQuantity(position) }
                minusButton.setOnClickListener { decreaseQuantity(position) }
                deleteButton.setOnClickListener { deleteItem(position) }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (cartQuantity[position] < 10) {
                cartQuantity[position]++
                binding.cartItemQuantity.text = cartQuantity[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (cartQuantity[position] > 1) {
                cartQuantity[position]--
                binding.cartItemQuantity.text = cartQuantity[position].toString()
            }
        }

        private fun deleteItem(position: Int) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val key = snapshot.children.elementAt(position).key
                    key?.let {
                        cartItemsReference.child(it).removeValue().addOnSuccessListener {
                            cartItems.removeAt(position)
                            cartItemPrices.removeAt(position)
                            cartImages.removeAt(position)
                            cartDescriptions.removeAt(position)
                            cartQuantity.removeAt(position)
                            cartIngredient.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, cartItems.size)
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

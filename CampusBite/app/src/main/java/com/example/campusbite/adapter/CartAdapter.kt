package com.example.campusbite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusbite.databinding.CartItemBinding
import com.example.campusbite.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartAdapter(
    private val cartItems: MutableList<CartItems>,
    private val listener: OnCartUpdatedListener
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val database = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val item = cartItems[position]
            val quantity = item.foodQuantity?.toIntOrNull() ?: 1

            binding.cartFoodName.text = item.foodName
            binding.cartItemPrice.text = item.foodPrice
            binding.cartItemQuantity.text = quantity.toString()

            Glide.with(binding.root.context)
                .load(item.foodImage)
                .into(binding.cartImage)

            // + Button
            binding.plusButton.setOnClickListener {
                var qty = item.foodQuantity?.toIntOrNull() ?: 1
                if (qty < 10) {
                    qty++
                    item.foodQuantity = qty.toString()
                    binding.cartItemQuantity.text = item.foodQuantity

                    updateQuantityInFirebase(item)
                }
            }

            // - Button
            binding.minusButton.setOnClickListener {
                var qty = item.foodQuantity?.toIntOrNull() ?: 1
                if (qty > 1) {
                    qty--
                    item.foodQuantity = qty.toString()
                    binding.cartItemQuantity.text = item.foodQuantity

                    updateQuantityInFirebase(item)
                }
            }

            // Delete Button
            binding.deleteButton.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    deleteItemFromFirebase(item)
                    cartItems.removeAt(pos)
                    notifyItemRemoved(pos)
                    notifyItemRangeChanged(pos, cartItems.size)
                    listener.onCartUpdated() // Notify total update after delete
                }
            }
        }

        private fun updateQuantityInFirebase(item: CartItems) {
            val itemId = item.itemId
            if (itemId.isNotEmpty()) {
                database.child("cart").child(userId).child(itemId)
                    .child("foodQuantity")
                    .setValue(item.foodQuantity)
                    .addOnSuccessListener {
                        listener.onCartUpdated()
                    }
            }
        }

        private fun deleteItemFromFirebase(item: CartItems) {
            val itemId = item.itemId
            if (itemId.isNotEmpty()) {
                database.child("cart").child(userId).child(itemId).removeValue()
            } else {
                Toast.makeText(binding.root.context, "Invalid item ID", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.campusbite.databinding.CartItemBinding
//import com.example.campusbite.model.CartItems
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//
//class CartAdapter(
//    private val cartItems: MutableList<CartItems>
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    // Quantity গুলো এখানেই handle করা হচ্ছে
//    private val itemQuantities = MutableList(cartItems.size) {
//        cartItems[it].foodQuantity?.toIntOrNull() ?: 1
//    }
//    private val database = FirebaseDatabase.getInstance().reference
//    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CartViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        holder.bind(position)
//    }
//
//    override fun getItemCount(): Int = cartItems.size
//
//    inner class CartViewHolder(private val binding: CartItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(position: Int) {
//            val item = cartItems[position]
//            val quantity = itemQuantities[position]
//
//            binding.cartFoodName.text = item.foodName
//            binding.cartItemPrice.text = item.foodPrice
//            binding.cartItemQuantity.text = quantity.toString()
//
//            Glide.with(binding.root.context)
//                .load(item.foodImage)
//                .into(binding.cartImage)
//
//            // Plus button
//            binding.plusButton.setOnClickListener {
//                if (itemQuantities[position] < 10) {
//                    itemQuantities[position]++
//                    binding.cartItemQuantity.text = itemQuantities[position].toString()
//
//                    // Update Firebase
//                    val updatedQty = itemQuantities[position].toString()
//                    val itemKey = cartItems[position].itemId  // Make sure itemId is present in CartItems
//                    database.child("cart").child(userId).child(itemKey).child("foodQuantity").setValue(updatedQty)
//                }
//            }
//
//// Minus button
//            binding.minusButton.setOnClickListener {
//                if (itemQuantities[position] > 1) {
//                    itemQuantities[position]--
//                    binding.cartItemQuantity.text = itemQuantities[position].toString()
//
//                    // Update Firebase
//                    val updatedQty = itemQuantities[position].toString()
//                    val itemKey = cartItems[position].itemId
//                    database.child("cart").child(userId).child(itemKey).child("foodQuantity").setValue(updatedQty)
//                }
//            }
//
//// Delete button
//            binding.deleteButton.setOnClickListener {
//                val itemPosition = adapterPosition
//                if (itemPosition != RecyclerView.NO_POSITION) {
//                    val itemKey = cartItems[itemPosition].itemId
//                    database.child("cart").child(userId).child(itemKey).removeValue()  // Firebase remove
//                    cartItems.removeAt(itemPosition)
//                    itemQuantities.removeAt(itemPosition)
//                    notifyItemRemoved(itemPosition)
//                    notifyItemRangeChanged(itemPosition, cartItems.size)
//                }
//            }
//        }
//    }
//}




//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.campusbite.adapter.PopulerAdapter.PopulerViewHolder
//import com.example.campusbite.databinding.CartItemBinding
//import com.example.campusbite.databinding.PopulerItemBinding
//
//class CartAdapter(
//    private val cartItems: MutableList<String>,
//    private val cartItemPrices: MutableList<String>, // ✅ নাম বদল
//    private val cartImages: MutableList<Int>
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    private val itemQuantities = IntArray(cartItems.size) { 1 }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CartViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        holder.bind(position)
//    }
//
//    override fun getItemCount(): Int = cartItems.size
//
//    inner class CartViewHolder(private val binding: CartItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(position: Int) {
//            binding.apply {
//                val quantity = itemQuantities[position]
//                cartFoodName.text = cartItems[position]
//                cartItemPrice.text = cartItemPrices[position] // ✅ fixed line
//                cartImage.setImageResource(cartImages[position])
//                cartItemQuantity.text = quantity.toString()
//
//                minusButton.setOnClickListener {
//                    decreaseQuantity(position)
//                }
//
//                plusButton.setOnClickListener {
//                    increaseQuantity(position)
//                }
//
//                deleteButton.setOnClickListener {
//                    val itemPosition = adapterPosition
//                    if (itemPosition != RecyclerView.NO_POSITION) {
//                        deleteItem(itemPosition)
//                    }
//                }
//            }
//        }
//
//        private fun deleteItem(position: Int) {
//            cartItems.removeAt(position)
//            cartItemPrices.removeAt(position)
//            cartImages.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, cartItems.size)
//        }
//
//        private fun increaseQuantity(position: Int) {
//            if (itemQuantities[position] < 10) {
//                itemQuantities[position]++
//                binding.cartItemQuantity.text = itemQuantities[position].toString()
//            }
//        }
//
//        private fun decreaseQuantity(position: Int) {
//            if (itemQuantities[position] > 1) {
//                itemQuantities[position]--
//                binding.cartItemQuantity.text = itemQuantities[position].toString()
//            }
//        }
//    }
//}

//class CartAdapter(
//    private val cartItems: MutableList<String>,
//    private val cartItemPrices: MutableList<String>,
//    private val cartImages: MutableList<Int>
//) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
//
//    private val itemQuantities = IntArray(cartItems.size) { 1 } // Default quantity = 1
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
//        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CartViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
//        holder.bind(
//            cartItems[position],
//            cartItemPrices[position],
//            cartImages[position],
//            itemQuantities[position],
//            onQuantityChange = { newQty ->
//                itemQuantities[position] = newQty
//                notifyItemChanged(position)
//            }
//        )
//    }
//
//    override fun getItemCount(): Int = cartItems.size
//
//    class CartViewHolder(private val binding: CartItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(
//            name: String,
//            price: String,
//            imageRes: Int,
//            quantity: Int,
//            onQuantityChange: (Int) -> Unit
//        ) {
//            binding.cartFoodName.text = name
//            binding.cartItemPrice.text = price
//            binding.cartImage.setImageResource(imageRes)
//            binding.cartItemQuantity.text = quantity.toString()
//
//            binding.plusButton.setOnClickListener {
//                val newQty = quantity + 1
//                onQuantityChange(newQty)
//            }
//
//            binding.minusButton.setOnClickListener {
//                val newQty = if (quantity > 1) quantity - 1 else 1
//                onQuantityChange(newQty)
//            }
//
//            binding.deleteButton.setOnClickListener {
//                fun deceaseQuantity(position: Int){
//
//                }
//            }
//        }
//    }
//}

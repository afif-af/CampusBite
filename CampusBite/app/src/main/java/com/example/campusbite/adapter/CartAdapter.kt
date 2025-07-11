package com.example.campusbite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbite.adapter.PopulerAdapter.PopulerViewHolder
import com.example.campusbite.databinding.CartItemBinding
import com.example.campusbite.databinding.PopulerItemBinding

class CartAdapter(
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>, // ✅ নাম বদল
    private val cartImages: MutableList<Int>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val itemQuantities = IntArray(cartItems.size) { 1 }

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
            binding.apply {
                val quantity = itemQuantities[position]
                cartFoodName.text = cartItems[position]
                cartItemPrice.text = cartItemPrices[position] // ✅ fixed line
                cartImage.setImageResource(cartImages[position])
                cartItemQuantity.text = quantity.toString()

                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }

                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }

                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun deleteItem(position: Int) {
            cartItems.removeAt(position)
            cartItemPrices.removeAt(position)
            cartImages.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }
    }
}

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

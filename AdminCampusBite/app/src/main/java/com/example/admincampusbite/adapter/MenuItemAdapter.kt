package com.example.admincampusbite.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincampusbite.R
import com.example.admincampusbite.databinding.ItemItemBinding
import com.example.admincampusbite.model.AllMenu

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = MutableList(menuList.size) { 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val menuItem = menuList[position]
            val imageString = menuItem.imageUrl

            if (!imageString.isNullOrEmpty()) {
                val uri = Uri.parse(imageString)
                Glide.with(context)
                    .load(uri)
                    .into(binding.orderFoodImage)
            } else {
                Glide.with(context)
                    .load(R.drawable.placeholder_image)
                    .into(binding.orderFoodImage)
            }

            binding.apply {
                foodName.text = menuItem.name ?: "No Name"
                foodPrice.text = "৳ ${menuItem.price ?: "N/A"}"
//

                cartItemQuantity.text = itemQuantities[position].toString()

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
            menuList.removeAt(position)
            itemQuantities.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)
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

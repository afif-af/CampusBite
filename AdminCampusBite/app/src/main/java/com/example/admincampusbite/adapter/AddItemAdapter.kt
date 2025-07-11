package com.example.admincampusbite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampusbite.databinding.ItemItemBinding

class AddItemAdapter(
    private val menuItemName: MutableList<String>,
    private val menuItemPrice: MutableList<String>,
    private val menuItemImage: MutableList<Int>
) : RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = MutableList(menuItemName.size) { 1 } // MutableList to support delete

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItemName.size

    inner class AddItemViewHolder(private val binding: ItemItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                nameCustomer.text = this@AddItemAdapter.menuItemName[position]
                orderFoodQuantity.text = this@AddItemAdapter.menuItemPrice[position]
                orderFoodImage.setImageResource(this@AddItemAdapter.menuItemImage[position])

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
            menuItemName.removeAt(position)
            menuItemPrice.removeAt(position)
            menuItemImage.removeAt(position)
            itemQuantities.removeAt(position) // ✅ Fix: also remove quantity
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuItemName.size)
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

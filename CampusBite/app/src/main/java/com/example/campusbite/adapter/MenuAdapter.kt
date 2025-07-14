package com.example.campusbite.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusbite.DetailsActiviy
import com.example.campusbite.databinding.MenuItemBinding
import com.example.campusbite.model.MenuItem

class MenuAdapter(
    private val menuItems: MutableList<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailActivity(position)
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val item = menuItems[position]
            val intent = Intent(requireContext, DetailsActiviy::class.java).apply {
                putExtra("MenuItemName", item.name)
                putExtra("MenuItemImage", item.imageUrl)
                putExtra("MenuItemDescription", item.description)
                putExtra("MenuItemIngredient", item.ingredient)
                putExtra("MenuItemPrice", item.price)
            }
            requireContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val item = menuItems[position]
            binding.menuFoodName.text = item.name
            binding.menuPrice.text = item.price

            Glide.with(requireContext)
                .load(Uri.parse(item.imageUrl))
                .into(binding.menuImage)
        }
    }
}

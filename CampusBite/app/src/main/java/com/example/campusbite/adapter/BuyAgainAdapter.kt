package com.example.campusbite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusbite.databinding.BuyAgainItemBinding
import com.example.campusbite.model.HistoryItem

class BuyAgainAdapter(private val items: List<HistoryItem>) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.buyAgainFoodName.text = item.foodName
            binding.buyAgainFoodPrice.text = item.foodPrice

            // যদি drawable রিসোর্স id হয় তাহলে নিচের লাইন এ পরিবর্তন করো:
            // binding.buyAgainFoodImage.setImageResource(item.foodImageResId)

            // Firebase বা url থেকে ছবি লোড করতে Glide ব্যবহার
            Glide.with(binding.buyAgainFoodImage.context)
                .load(item.foodImageUrl)
                .placeholder(com.example.campusbite.R.drawable.photomenu1) // placeholder image
                .into(binding.buyAgainFoodImage)
        }
    }
}

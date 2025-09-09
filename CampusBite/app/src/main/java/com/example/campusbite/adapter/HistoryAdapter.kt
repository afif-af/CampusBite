package com.example.campusbite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusbite.databinding.HistoryItemBinding
import com.example.campusbite.model.HistoryItem

class HistoryAdapter(
    private val context: Context,
    private var historyList: MutableList<HistoryItem>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.historyFoodName.text = item.foodName
            binding.historyFoodPrice.text = item.foodPrice

            Glide.with(context)
                .load(item.foodImage)
                .into(binding.historyFoodImage)

            binding.buyAgainFoodButton.setOnClickListener {
                // এখানে চাইলে আবার cart এ add করার logic দিতে পারো
                // Toast.makeText(context, "${item.foodName} added to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size

    fun updateList(newList: MutableList<HistoryItem>) {
        historyList.clear()
        historyList.addAll(newList)
        notifyDataSetChanged()
    }
}

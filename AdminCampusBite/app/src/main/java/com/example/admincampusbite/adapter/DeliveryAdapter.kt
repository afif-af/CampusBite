package com.example.admincampusbite.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampusbite.databinding.DeliveryItemBinding

class DeliveryAdapter(
    private val customerNames: MutableList<String>,
    private val moneyStatuses: MutableList<String> // Expected values: "Received" or "Not Received"
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val statusText = moneyStatuses[position]

            // Use binding to access views
            binding.apply {
                customerName.text = customerNames[position]
                moneyStatus.text = statusText

                // Map string status to color
                val colorMap = mapOf(
                    "Received" to Color.GREEN,
                    "Not Received" to Color.RED
                )
                val statusColorValue = colorMap[statusText] ?: Color.BLACK

                moneyStatus.setTextColor(statusColorValue)
                statusColor.backgroundTintList = ColorStateList.valueOf(statusColorValue)
            }
        }
    }
}

package com.example.admincampusbite.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampusbite.databinding.DeliveryItemBinding
import kotlin.collections.get

class DeliveryAdapter(
    private val customerNames: ArrayList<String>,
    private val moneyStatuses: ArrayList<String>
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeliveryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DeliveryViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class DeliveryViewHolder(private val binding: DeliveryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                val status = moneyStatuses[position].lowercase()
                customerName.text = customerNames[position]
                moneyStatus.text = moneyStatuses[position]

                val colorMap = mapOf(
                    "received" to Color.GREEN,
                    "notreceived" to Color.RED,
                    "pending" to Color.GRAY
                )

                moneyStatus.setTextColor(colorMap[status] ?: Color.BLACK)
                statusColor.backgroundTintList = ColorStateList.valueOf(colorMap[status] ?: Color.BLACK)
            }
        }

    }
}




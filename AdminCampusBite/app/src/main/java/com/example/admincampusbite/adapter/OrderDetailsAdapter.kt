package com.example.admincampusbite.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincampusbite.databinding.OrderdetailsitemBinding

class OrderDetailsAdapter(
    private var context: Context,
    private var foodName: MutableList<String>,
    private var foodImages: MutableList<String>,
    private var foodQuantity: MutableList<Int>,
    private var foodPrices: MutableList<String>
) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
        val binding = OrderdetailsitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
        holder.bind(
            context,
            foodName[position],
            foodPrices[position],
            foodQuantity[position],
            foodImages[position]
        )
    }

    override fun getItemCount(): Int = foodName.size

  inner class OrderDetailsViewHolder(private val binding: OrderdetailsitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            context: Context,
            name: String,
            price: String,
            quantity: Int,
            imageUrl: String
        ) {
            binding.apply {
                nameFood.text = name
                priceFood.text = "$price ৳"
                quantityFood.text = "x$quantity"

                Glide.with(context)
                    .load(Uri.parse(imageUrl))
                    .into(imageFood)
            }
        }
    }
}

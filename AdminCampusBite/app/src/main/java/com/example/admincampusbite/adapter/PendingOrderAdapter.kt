package com.example.admincampusbite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.admincampusbite.databinding.PendingorderitemBinding
import java.security.cert.TrustAnchor

class PendingOrderAdapter(
    private val customerName: ArrayList<String>,
    private val quantity: ArrayList<String>,
    private val foodImage: ArrayList<Int>,
    private val context: Context
) : RecyclerView.Adapter<PendingOrderAdapter.PrendingOrderViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrendingOrderViewHolder {
        val binding = PendingorderitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrendingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerName.size

    inner class PrendingOrderViewHolder(private val binding: PendingorderitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted=false

        fun bind(position: Int) {
            binding.apply {
                nameCustomer.text = customerName[position]
                orderFoodQuantity.text = quantity[position]
                orderFoodImage.setImageResource(foodImage[position])

                orderAcceptButton.apply {
                    if(!isAccepted)
                    {
                        text="Accepted"
                    }
                    else
                    {
                        text="Dispatch"
                    }
                    setOnClickListener {
                        if(!isAccepted)
                        {
                            text="Dispatch"
                            isAccepted= true
                            showTost("Order is Accepted")

                        }
                        else
                        {
                            customerName.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showTost("Order is Dispatch")

                        }


                    }
                }
            }

        }
        private  fun showTost(message: String)
        {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()

        }
    }
}

package com.example.admincampusbite.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincampusbite.PendingOrderActivity
import com.example.admincampusbite.databinding.PendingorderitemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val quantity: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked: PendingOrderActivity,
) : RecyclerView.Adapter<PendingOrderAdapter.PrendingOrderViewHolder>() {

    interface OnItemClicked{
        fun onItemClickListner(position: Int)
        fun onItemAcceptedClickListner(position: Int)
        fun onItemDispatchClickListner(position: Int)
//        fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String)

    }

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

                val uriString=foodImage[position]
                val uri= Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderFoodImage)

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
                            itemClicked.onItemAcceptedClickListner(position)
                        }
                        else
                        {
                            customerName.removeAt(position)
                            notifyItemRemoved(position)
                            showTost("Order is Dispatch")
                            itemClicked.onItemDispatchClickListner(position)

                        }


                    }
                    itemView.setOnClickListener {
                        itemClicked.onItemClickListner(position)
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

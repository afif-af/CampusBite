package com.example.campusbite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campusbite.databinding.NotificationItemBinding

class NotificationsAdapter(private var notification: ArrayList<String>,
                           private var notificationImage: ArrayList<Int>
     ): RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsAdapter.NotificationViewHolder {
        val binding= NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: NotificationsAdapter.NotificationViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int=notification.size

    inner class NotificationViewHolder(private  val binding: NotificationItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int)
        {
            binding.apply {
                notificationTextView.text=notification[position]
                notificationImageView.setImageResource(notificationImage[position])


            }


        }

    }


}
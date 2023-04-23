package com.example.socializer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.model.Chat
import com.example.socializer.model.Chatlist
import com.example.socializer.model.Forum

class ChatlistAdapter (var mList : List<Chatlist>) : RecyclerView.Adapter<ChatlistAdapter.ChatlistViewHolder>() {
    inner class ChatlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.profile_image)
        val username: TextView = itemView.findViewById(R.id.username)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
    }

    fun setFilteredList(mList: List<Chatlist>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chatlist_item, parent, false)
        return ChatlistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ChatlistViewHolder, position: Int) {
        holder.image.setImageURI(mList[position].responder.imageuri?.toUri())
        holder.username.text = mList[position].responder.username
        holder.lastMessage.text = mList[position].messages?.last()?.message
        holder.timestamp.text = mList[position].messages?.last()?.timestamp.toString()
    }
}
package com.example.socializer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.model.Forum

class ForumAdapter (var mList : List<Forum>) : RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {
    inner class ForumViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val logo : ImageView = itemView.findViewById(R.id.forumLogo)
        val title : TextView = itemView.findViewById(R.id.forumTitle)
    }

    fun setFilteredList(mList: List<Forum>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forum_item, parent, false)
        return ForumViewHolder(view)
    }

    override fun getItemCount(): Int {
       return mList.size
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        holder.logo.setImageResource(mList[position].logo)
        holder.title.text = mList[position].title
    }
}
package com.example.socializer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.model.Post

class PostForumAdapter (var mList : List<Post>) : RecyclerView.Adapter<PostForumAdapter.PostViewHolder>() {
    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.feed_post_title)
        val img : ImageView = itemView.findViewById(R.id.feed_post_image_optional)
        val logo : ImageView = itemView.findViewById(R.id.feed_forum_image)
        val forumName : TextView = itemView.findViewById(R.id.feed_forum_title)
        //        val video : VideoView = itemView.findViewById(R.id.post_video_optional)
        val content : TextView = itemView.findViewById(R.id.post_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_feed, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.title.text = mList[position].title
        holder.img.setImageURI(mList[position].imageUri?.toUri())
        holder.content.text = mList[position].description
        holder.logo.setImageResource(mList[position].forum.logo)
        holder.forumName.text = mList[position].forum.title
    }
}
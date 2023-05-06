package com.example.socializer.adapter

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Interpolator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.helpers.deleteVote
import com.example.socializer.helpers.vote
import com.example.socializer.model.Forum
import com.example.socializer.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PostFeedAdapter (var mList : List<Post>) : RecyclerView.Adapter<PostFeedAdapter.PostViewHolder>() {
    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.feed_post_title)
        val img : ImageView = itemView.findViewById(R.id.feed_post_image_optional)
        val logo : ImageView = itemView.findViewById(R.id.feed_forum_image)
        val forumName : TextView = itemView.findViewById(R.id.feed_forum_title)
        // val video : VideoView = itemView.findViewById(R.id.post_video_optional)
        val feed_upvote : ImageView = itemView.findViewById(R.id.feed_upvote)
        val score : TextView = itemView.findViewById(R.id.feed_post_score)
        val feed_downvote : ImageView = itemView.findViewById(R.id.feed_downvote)
        val feed_comment : ImageView = itemView.findViewById(R.id.feed_comment)
        val feed_share : ImageView = itemView.findViewById(R.id.feed_share_iv)
        val content : TextView = itemView.findViewById(R.id.feed_post_content)
    }

    fun setFilteredList(mList: List<Post>){
        this.mList = mList
        notifyDataSetChanged()
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
        Picasso.get().load(mList[position].imageUri!!).into(holder.img)
        holder.content.text = mList[position].description

        // set title and img for forum
        val forumId = mList[position].forum
        val db = FirebaseDatabase.getInstance().reference.child("forums").child(forumId!!)
        db.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    holder.forumName.text = snapshot.child("title").value.toString()
                    Picasso.get().load(snapshot.child("logo").value.toString()).into(holder.logo)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        setScore(mList[position].id!!, holder)
        checkVote(mList[position].id!!, holder)

        // upvote
        holder.feed_upvote.setOnClickListener {
            if (holder.feed_upvote.tag != "upvoted") {
                vote(mList[position].id!!, 1)
            } else {
                holder.feed_upvote.tag = ""
                deleteVote(mList[position].id!!)
            }

            checkVote(mList[position].id!!, holder)
        }

        // downvote
        holder.feed_downvote.setOnClickListener {
            if (holder.feed_downvote.tag != "downvoted") {
                vote(mList[position].id!!, 0)
            } else {
                holder.feed_downvote.tag = ""
                deleteVote(mList[position].id!!)
            }

            checkVote(mList[position].id!!, holder)
        }

        // comment

        // share
    }


    private fun checkVote(post: String, holder: PostFeedAdapter.PostViewHolder) {
        val database = Firebase.database.reference.child("likes").child(post).child(FirebaseAuth.getInstance().currentUser!!.uid)

        database.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    if (snapshot.child("positive").getValue<Int>() == 1) {
                        holder.feed_upvote.backgroundTintList = ColorStateList.valueOf(Color.rgb(255,99,71))
                        holder.feed_downvote.backgroundTintList = ColorStateList.valueOf(Color.rgb(151,151,151))
                        holder.feed_upvote.tag = "upvoted"
                    } else {
                        holder.feed_downvote.tag = "downvoted"
                        holder.feed_upvote.backgroundTintList = ColorStateList.valueOf(Color.rgb(151,151,151))
                        holder.feed_downvote.backgroundTintList = ColorStateList.valueOf(Color.rgb(255,99,71))
                    }

                } else {
                    holder.feed_upvote.backgroundTintList = ColorStateList.valueOf(Color.rgb(151,151,151))
                    holder.feed_downvote.backgroundTintList = ColorStateList.valueOf(Color.rgb(151,151,151))
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun setScore(post : String, holder: PostViewHolder) {
        var score = 0
        val reference = FirebaseDatabase.getInstance().reference.child("likes")
            .child(post)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (vote in snapshot.children) {
                    if (vote.child("positive").getValue<Int>() == 1) {
                        score += 1
                    } else if (vote.child("positive").getValue<Int>() == 0)
                    {
                        score -= 1
                    }
                }
                holder.score.text = score.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }


}
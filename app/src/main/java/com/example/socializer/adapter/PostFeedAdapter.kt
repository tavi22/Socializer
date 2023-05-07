package com.example.socializer.adapter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.helpers.deleteVote
import com.example.socializer.helpers.vote
import com.example.socializer.model.Post
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream


class PostFeedAdapter (var mList : List<Post>, var context : Context) : RecyclerView.Adapter<PostFeedAdapter.PostViewHolder>() {
    inner class PostViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val postTitle : TextView = itemView.findViewById(R.id.feed_post_title)
        val postImg : ImageView = itemView.findViewById(R.id.feed_post_image_optional)
        val forumLogo : ImageView = itemView.findViewById(R.id.feed_forum_image)
        val forumName : TextView = itemView.findViewById(R.id.feed_forum_title)
        // val video : VideoView = itemView.findViewById(R.id.post_video_optional)
        val feed_upvote : ImageView = itemView.findViewById(R.id.feed_upvote)
        val score : TextView = itemView.findViewById(R.id.feed_post_score)
        val feed_downvote : ImageView = itemView.findViewById(R.id.feed_downvote)
        val feed_comment : ImageView = itemView.findViewById(R.id.feed_comment)
        val feed_share_iv : ImageView = itemView.findViewById(R.id.feed_share_iv)
        val feed_share_tv : TextView = itemView.findViewById(R.id.feed_share_tv)
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
        holder.postTitle.text = mList[position].title
        Picasso.get().load(mList[position].imageUri!!).into(holder.postImg)
        holder.content.text = mList[position].description

        // set title and img for forum
        val forumId = mList[position].forum
        val db = FirebaseDatabase.getInstance().reference.child("forums").child(forumId!!)
        db.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    holder.forumName.text = snapshot.child("title").value.toString()
                    Picasso.get().load(snapshot.child("logo").value.toString()).into(holder.forumLogo)
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
        holder.feed_comment.setOnClickListener { comment() }

        // share
        holder.feed_share_tv.setOnClickListener { // check permissions
            if (checkSelfPermission(
                    FirebaseApp.getInstance().applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    FirebaseApp.getInstance().applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) sharePost(holder)
            else {
                requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 101
                )
            } }
        holder.feed_share_iv.setOnClickListener { // check permissions
            if (checkSelfPermission(
                    FirebaseApp.getInstance().applicationContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    FirebaseApp.getInstance().applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) sharePost(holder)
            else {
                requestPermissions(
                    context as Activity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), 1
                )
            } }
    }

    private fun comment() {
        TODO("Not yet implemented")
    }


    private fun sharePost(holder: PostViewHolder) {
        val bitmap = holder.postImg.drawable as BitmapDrawable
        val uri = getImageToShare(bitmap.bitmap)
        context.grantUriPermission(context.packageName, uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out this post on Socializer! \n " + "\"" + holder.content.text + "\"")
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "share image")

        val resInfoList: List<ResolveInfo> = context.packageManager
            .queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }


        startActivity(context, shareIntent, null)
    }

    // Retrieving the url to share
    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(context.filesDir, "my_images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(context, "com.example.socializer.fileprovider", file)
        } catch (e: Exception) {
            Toast.makeText(context, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
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
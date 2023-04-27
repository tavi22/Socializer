package com.example.socializer.helpers

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.example.socializer.activity.MainActivity
import com.example.socializer.model.Forum
import com.example.socializer.model.Post
import com.example.socializer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


private lateinit var database : DatabaseReference
private lateinit var storageRef : StorageReference

fun saveNewAccount(email : String?, username : String, imageUri: String?) {
    val userId =  FirebaseAuth.getInstance().currentUser!!.uid
    val newUser = User(username, email, "New to Socializer", imageUri, false)
    database = Firebase.database.reference

    database.child("users").child(userId).setValue(newUser)
}

fun uploadProfilePicture(photoUrl: Uri?, email: String?, ctx : Context) {
    storageRef = FirebaseStorage.getInstance().getReference("profile-images/$email")
    photoUrl?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(ctx, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(ctx, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
    }
    }
}

fun uploadForumLogo(title : String?, description : String?, photoUrl: Uri?, owner : String?, ctx : Context) {
    database = Firebase.database.reference.child("forums")

    val id = database.push().key!!

    storageRef = FirebaseStorage.getInstance().getReference("forum-logos/$id")
    if (photoUrl != null) {
        storageRef.putFile(photoUrl).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val forum = Forum(title, description, uri.toString(), owner, arrayListOf(owner!!))

                    database.child(id).setValue(forum)
                    Toast.makeText(ctx, "Forum logo uploaded sucessfully", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(ctx, "Failed to upload forum logo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun uploadPostPhoto(title : String?, body : String?, videoUrl : String?, photoUrl: String?, owner : String?, ctx : Context) {
    database = Firebase.database.reference.child("posts")
    val id = database.push().key!!

    storageRef = FirebaseStorage.getInstance().getReference("post-images/$id")
    if (photoUrl != null) {
        storageRef.putFile(photoUrl.toUri()).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val post = Post(title, body, uri.toString(), videoUrl, owner, "dada")
                    database.child(id).setValue(post)
                    Toast.makeText(ctx, "Post image uploaded sucessfully", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(ctx, "Failed to upload post image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
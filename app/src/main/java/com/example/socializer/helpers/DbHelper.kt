package com.example.socializer.helpers

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
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

fun uploadForumLogo(photoUrl: Uri?, forumId : String, ctx : Context): String {
    var downloadUrl = photoUrl.toString()
    storageRef = FirebaseStorage.getInstance().getReference("forum-logos/$forumId")
    if (photoUrl != null) {
        storageRef.putFile(photoUrl).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                    Toast.makeText(ctx, "Forum logo uploaded sucessfully", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(ctx, "Failed to upload forum logo", Toast.LENGTH_SHORT).show()
            }
        }
    }
    return downloadUrl
}

fun uploadPostPhoto(photoUrl: String?, postId : String, ctx : Context): String {
    var downloadUrl = photoUrl.toString()
    storageRef = FirebaseStorage.getInstance().getReference("post-images/$postId")
    if (photoUrl != null) {
        storageRef.putFile(photoUrl.toUri()).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    downloadUrl = uri.toString()
                    Toast.makeText(ctx, "Post image uploaded sucessfully", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(ctx, "Failed to upload post image", Toast.LENGTH_SHORT).show()
            }
        }
    }
    return downloadUrl
}
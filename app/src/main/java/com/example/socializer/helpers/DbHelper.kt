package com.example.socializer.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.socializer.model.Forum
import com.example.socializer.model.Post
import com.example.socializer.model.User
import com.example.socializer.model.Vote
import com.google.android.gms.auth.api.signin.GoogleSignIn.requestPermissions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


private lateinit var database : DatabaseReference
private lateinit var storageRef : StorageReference
private var owner = FirebaseAuth.getInstance().currentUser?.uid.toString()


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

fun uploadForumLogo(title : String?, description : String?, photoUrl: Uri?, ctx : Context) {
    database = Firebase.database.reference.child("forums")

    val id = database.push().key!!

    storageRef = FirebaseStorage.getInstance().getReference("forum-logos/$id")
    if (photoUrl != null) {
        storageRef.putFile(photoUrl).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val forum = Forum(id, title, description, uri.toString(), owner, arrayListOf(owner))

                    database.child(id).setValue(forum)
                    Toast.makeText(ctx, "Forum logo uploaded sucessfully", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(ctx, "Failed to upload forum logo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun uploadPostPhoto(title : String?, body : String?, videoUrl : String?, photoUrl : String?, forumId : String?, ctx : Context) {
    database = Firebase.database.reference.child("posts")
    val id = database.push().key!!
    storageRef = FirebaseStorage.getInstance().getReference("post-images/$id")

    if (photoUrl != null) {
        storageRef.putFile(photoUrl.toUri()).addOnCompleteListener {
            if (it.isSuccessful) {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val post = Post(id, title, body, uri.toString(), videoUrl, owner, forumId)
                    database.child(id).setValue(post)
                    Toast.makeText(ctx, "Post image uploaded sucessfully", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(ctx, "Failed to upload post image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun vote (post : String, type : Int) {
    database = Firebase.database.reference.child("likes").child(post).child(owner)

    val vote = Vote(owner, post, type)
    database.setValue(vote)

}

fun deleteVote(post : String) {
    database = Firebase.database.reference.child("likes").child(post).child(owner)
    database.removeValue()
}
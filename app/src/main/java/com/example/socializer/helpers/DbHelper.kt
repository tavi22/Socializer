package com.example.socializer.helpers

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.socializer.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


private lateinit var dbRef : DatabaseReference
private lateinit var storageRef : StorageReference

fun saveNewAccount(email : String?, username : String?, imageUri: Uri?, ctx: Context) {
    dbRef = FirebaseDatabase.getInstance().getReference("users")
    val userId =  FirebaseAuth.getInstance().currentUser!!.uid
    val newUser = User(userId, username, email, "New to Socializer", imageUri, false)

    dbRef.child(userId).setValue(newUser).addOnCompleteListener {
        if (it.isSuccessful) {
            Toast.makeText(ctx, "Account created successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(ctx, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener {
        Toast.makeText(ctx, it.message.toString(), Toast.LENGTH_SHORT).show()
    }
}

fun saveNewPost() {}

fun saveNewForum() {}

fun saveNewMessage() {}

fun saveNewNotification() {}
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
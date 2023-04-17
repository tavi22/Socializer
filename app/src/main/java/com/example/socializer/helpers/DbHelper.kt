package com.example.socializer.helpers

import android.net.Uri
import com.example.socializer.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class DbHelper {
    private lateinit var dbRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    fun saveNewAccount(email : String?, username : String?, imageUri: Uri?) {
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val userId = dbRef.push().key!!

        val newUser = User(userId, username, email, "New to Socializer", imageUri, false)
        dbRef.child(userId).setValue(newUser)
    }

    fun saveNewPost() {}

    fun saveNewForum() {}

    fun saveNewMessage() {}

    fun saveNewNotification() {}
    fun uploadProfilePicture(photoUrl: Uri?, email: String?) {
        storageRef = FirebaseStorage.getInstance().getReference("profile-images/$email")
        photoUrl?.let { it1 -> storageRef.putFile(it1) }
    }
}
package com.example.socializer.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.socializer.R
import com.example.socializer.databinding.ActivityAddForumBinding
import com.example.socializer.databinding.ActivityPostBinding
import com.example.socializer.helpers.DEFAULT_FORUM_LOGO
import com.example.socializer.helpers.uploadForumLogo
import com.example.socializer.model.Forum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference

class AddForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddForumBinding
    private var imageUri: Uri? = DEFAULT_FORUM_LOGO.toUri()
    private lateinit var database : DatabaseReference
    private lateinit var storageRef : StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener { finish() }

        binding.addForumPhoto.setOnClickListener { resultLauncher.launch("image/*") }

        binding.createButton.setOnClickListener { newForum() }
    }

    private fun newForum() {
        val title = binding.forumTitle.text.toString()
        val description = binding.forumDescription.text.toString()
        val owner = FirebaseAuth.getInstance().currentUser?.uid.toString()

        uploadForumLogo(title, description, imageUri, owner, this)

        startActivity(Intent(this, MainActivity::class.java))
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imagePreview.setImageURI(it)
    }
}
package com.example.socializer.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socializer.databinding.ActivityPostBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener { finish() }

        binding.addPhoto.setOnClickListener { resultLauncher.launch(
                                                                ImagePicker.with(this)
                                                                    .provider(ImageProvider.BOTH)
                                                                    .crop()
                                                                    .cropFreeStyle()
                                                                    .createIntent()
                                                                    ) }

        binding.addVideo.setOnClickListener {
            videoResultLauncher.launch("video/*")
            val vid = binding.videoPreview
            vid.visibility = View.VISIBLE
            val mediaController = MediaController(this)
            mediaController.setAnchorView(vid)
            vid.setMediaController(mediaController)
            vid.requestFocus()
            vid.start()

        }

        binding.next.setOnClickListener { goNext() }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUri = it.data?.data!!
            binding.imagePreview.setImageURI(imageUri)
            imageUri?.let { _ ->
                contentResolver.takePersistableUriPermission(
                    imageUri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            //////////////
        }
    }

    private val videoResultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        videoUri = it
        binding.videoPreview.setVideoURI(it)
    }

    private fun goNext() {
        val intent = Intent(this, SelectForumActivity::class.java)
        intent.putExtra("img", imageUri.toString())
        intent.putExtra("title", binding.title.text.toString())
        intent.putExtra("body", binding.body.text.toString())
        intent.putExtra("vid", videoUri.toString())
        startActivity(intent)
    }
}
package com.example.socializer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.socializer.R
import com.example.socializer.model.Forum

class ForumDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum_details)

        findViewById<TextView>(R.id.test).text = intent.getParcelableExtra<Forum>("name")?.title
    }
}
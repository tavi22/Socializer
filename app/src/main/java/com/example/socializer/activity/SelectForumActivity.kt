package com.example.socializer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.adapter.ForumAdapter
import com.example.socializer.helpers.uploadPostPhoto
import com.example.socializer.model.Forum
import com.example.socializer.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class SelectForumActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var add : ImageView
    private var mList = ArrayList<Forum>()
    private lateinit var adapter: ForumAdapter
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_forum)

        val close = findViewById<ImageView>(R.id.close)
        close.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        add = findViewById(R.id.post_button)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()
        adapter = ForumAdapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        add.setOnClickListener { newPost() }
    }

    private fun newPost() {
        val title : String = intent.getStringExtra("title").toString()
        val body : String = intent.getStringExtra("body").toString()
        val vid : String = intent.getStringExtra("vid").toString()
        val uri : String = intent.getStringExtra("img").toString()
        val owner = FirebaseAuth.getInstance().currentUser!!.uid

        uploadPostPhoto(title, body, vid, uri, owner, this)

        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<Forum>()
            for (i in mList) {
                if (i.title?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList() {

    }
}
package com.example.socializer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.adapter.ForumAdapter
import com.example.socializer.model.Forum
import java.util.*
import kotlin.collections.ArrayList

class SelectForumActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<Forum>()
    private lateinit var adapter: ForumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_forum)

        val close = findViewById<ImageView>(R.id.close)
        close.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

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
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<Forum>()
            for (i in mList) {
                if (i.title.lowercase(Locale.ROOT).contains(query)) {
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
        mList.add(Forum("1", "FMI - Unibuc", "Forum for FMI Unibuc", R.drawable.google_logo, "eu"))
        mList.add(Forum("2", "Automatica Poli", "Forum for ASC Poli", R.drawable.logo_size, "eu"))
        mList.add(Forum("3", "ETTI - Politehnica", "Forum for ETTI Poli", R.drawable.ic_launcher_foreground, "eu"))
        mList.add(Forum("4", "Business si Turism - ASE", "Forum for BT ASE", R.drawable.ic_launcher_background, "eu"))

    }
}
package com.example.socializer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socializer.R
import com.example.socializer.adapter.PostFeedAdapter
import com.example.socializer.adapter.PostForumAdapter
import com.example.socializer.model.Forum
import com.example.socializer.model.Post
import com.example.socializer.model.User
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [ExploreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ExploreFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mList = ArrayList<Post>()
    private lateinit var adapter : PostFeedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        addData()

        adapter = PostFeedAdapter(mList)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        return view
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<Post>()
            for (i in mList) {
                if (i.title.lowercase(Locale.ROOT).contains(query) || i.description.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(context, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addData() {
        val user = User("dada", "mimi@mail.com", "papa", "1234")
        mList.add(Post("1", "Titlu mare", "Salut carei treaba", "nu", "nu", user, Forum("1","FMI", "Blabla", R.drawable.google_logo,"1")))
        mList.add(Post("2", "Examen", "jambala", "nu", "nu", user, Forum("1","FMI", "Blabla",R.drawable.google_logo,"1")))
        mList.add(Post("3", "Intrare", "carolaina", "nu", "nu", user, Forum("1","FMI", "Blabla",R.drawable.google_logo,"1")))
        mList.add(Post("4", "Meditatii matematica", "Caut meditator fast", "nu", "nu", user, Forum("1","FMI", "Blabla",R.drawable.google_logo,"1")))

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExploreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
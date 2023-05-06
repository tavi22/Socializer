package com.example.socializer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.example.socializer.R
import com.example.socializer.adapter.PostFeedAdapter
import com.example.socializer.databinding.ActivityMainBinding
import com.example.socializer.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference
    private var mList = ArrayList<Post>()
    private lateinit var adapter : PostFeedAdapter
    private lateinit var postCount : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        val username = view.findViewById<TextView>(R.id.username)
        val profilePicure = view.findViewById<ImageView>(R.id.profile_picture)
        val descpription = view.findViewById<TextView>(R.id.bio)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        postCount = view.findViewById(R.id.posts_count)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.uid)
        }
        dbRef.get().addOnSuccessListener {
            if (it.exists()) {
                username.text = it.child("username").value.toString()
                Glide.with(this).load(it.child("imageuri").value.toString().toUri()).into(profilePicure)
                descpription.text = it.child("description").value.toString()
            }
        }

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        addData(recyclerView)

        return view

    }

        private fun addData(recyclerView: RecyclerView) {
            dbRef = Firebase.database.reference.child("posts")

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (postSnapshot in snapshot.children) {
                            val post = postSnapshot.getValue(Post::class.java)
                            if (post!!.owner == FirebaseAuth.getInstance().currentUser!!.uid) {
                                mList.add(post)
                            }
                        }
                        postCount.text = mList.size.toString()
                    }

                    adapter = PostFeedAdapter(mList)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
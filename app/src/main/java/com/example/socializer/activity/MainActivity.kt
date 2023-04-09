package com.example.socializer.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.socializer.R
import com.example.socializer.databinding.ActivityMainBinding
import com.example.socializer.fragments.ProfileFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController : NavController
    private lateinit var topAppBar : MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        // Set up Top Bar
        topAppBar = findViewById(R.id.top_bar)
        setSupportActionBar(topAppBar)
        topAppBar.setNavigationOnClickListener {
            // to do!!
            Toast.makeText(this, "Pressed button", Toast.LENGTH_SHORT).show()
        }

        // Set up Bottom Nav
        val navContainerFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as  NavHostFragment
        navController = navContainerFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)

        setupWithNavController(bottomNavView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.top_bar_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.profile -> replaceFragment(ProfileFragment())

            R.id.logout -> {
                firebaseAuth.signOut()
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}
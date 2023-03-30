package com.example.socializer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socializer.databinding.ActivityLogInBinding
import com.example.socializer.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.button.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent);
        }
        
        val navContainerFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as  NavHostFragment
        navController = navContainerFragment.navController

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavView)

        setupWithNavController(bottomNavView, navController)
    }
}
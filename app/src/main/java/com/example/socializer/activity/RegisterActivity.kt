package com.example.socializer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import com.example.socializer.databinding.ActivityRegisterBinding
import com.example.socializer.helpers.DEFAULT_PROFILE_IMAGE
import com.example.socializer.helpers.saveNewAccount
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()
            val username = binding.usernameEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            saveNewAccount(email, username, DEFAULT_PROFILE_IMAGE)
                            val intent = Intent(this, LogInActivity::class.java)
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }


                } else {
                    Toast.makeText(this, "Passwords are not matching!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill all the empty fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
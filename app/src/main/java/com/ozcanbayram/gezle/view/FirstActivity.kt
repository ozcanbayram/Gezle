package com.ozcanbayram.gezle.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ozcanbayram.gezle.databinding.ActivityFirstBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FirstActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFirstBinding
    //For Firebase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_first)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.gezle.setOnClickListener {
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
        }

        auth=Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser!=null){
            val  intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
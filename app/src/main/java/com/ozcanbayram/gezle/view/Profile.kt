package com.ozcanbayram.gezle.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ozcanbayram.gezle.R
import com.ozcanbayram.gezle.databinding.ActivityLoginBinding
import com.ozcanbayram.gezle.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}
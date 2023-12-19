package com.ozcanbayram.gezle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ozcanbayram.gezle.databinding.ActivityWelcomeBinding

class Welcome : AppCompatActivity() {
    private lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.kaydol.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }

        binding.giris.setOnClickListener {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

    }
}
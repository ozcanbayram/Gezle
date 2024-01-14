package com.ozcanbayram.gezle.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.ozcanbayram.gezle.R
import com.ozcanbayram.gezle.databinding.ActivityDetailsBinding
import com.ozcanbayram.gezle.databinding.ActivityWaitBinding

class WaitActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWaitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaitBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 3 saniye sayma işlemini başlat
        val sayac = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Sayma işlemi devam ediyor
            }

            override fun onFinish() {
                // Sayma işlemi bitti, başka bir aktiviteye git
                val intent = Intent(this@WaitActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        sayac.start()

    }
}
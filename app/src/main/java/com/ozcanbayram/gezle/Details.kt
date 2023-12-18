package com.ozcanbayram.gezle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ozcanbayram.gezle.databinding.ActivityDetailsBinding
import com.ozcanbayram.gezle.databinding.ActivityMapsBinding

class Details : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //MapsActivity'den gelen yer ismini textView5 e yazdırmak:
        val intentForPlaceName = intent
        val place_name =intentForPlaceName.getStringExtra("place_name")
        binding.textView5   .text = place_name.toString()

    }

    fun select_image(view : View){

    }

    fun share(view : View){

    }

}
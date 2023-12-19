package com.ozcanbayram.gezle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ozcanbayram.gezle.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ozcanbayram.gezle.databinding.ActivityMapsBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    //For Fİrebase
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //Menunyu aktiviteyle bagla
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.place_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Menuden eleman secilirse ne olacak
        if(item.itemId == R.id.add_place){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        if(item.itemId == R.id.sign_out){
            auth.signOut()
            val intent = Intent(this,FirstActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
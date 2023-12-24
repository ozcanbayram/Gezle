package com.ozcanbayram.gezle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ozcanbayram.gezle.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.ozcanbayram.gezle.databinding.ActivityMapsBinding
import com.ozcanbayram.gezle.model.Post
import java.sql.Time
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    //For Fİrebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var postArrayList : ArrayList<Post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth
        db = Firebase.firestore

        postArrayList = ArrayList<Post>()

        getData()

    }

    private fun getData(){
        db.collection("Posts").addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(this, error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value != null){
                    if(!value.isEmpty){

                        val documents = value.documents
                        for (document in documents){

                            val downloadUrl = document.get("downloadUrl") as String
                            val email = document.get("email") as String
                            val ad_soyad = document.get("ad_soyad") as String
                            val comment = document.get("comment") as String
                            val latitudeInfo = document.get("latitudeInfo") as String
                            val longitudeInfo = document.get("longitudeInfo") as String
                            val place_name = document.get("place_name") as String

                            val post = Post(downloadUrl,email,ad_soyad,comment,latitudeInfo,longitudeInfo,place_name)
                            postArrayList.add(post)

                            /*
                            println("URL: " + downloadUrl)
                            println("E-Posta: " + email)
                            println("Ad Soyad: " + ad_soyad)
                            println("Yorum: " + comment)
                            println("Enlem: " + latitudeInfo)
                            println("Boylam: " + longitudeInfo)
                            println("Yer ismi: " + place_name)
                             */

                        }

                    }
                }
            }
        }
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
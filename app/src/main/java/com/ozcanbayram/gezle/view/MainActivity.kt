package com.ozcanbayram.gezle.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ozcanbayram.gezle.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.ozcanbayram.gezle.R
import com.ozcanbayram.gezle.adapter.MainRecyclerAdapter
import com.ozcanbayram.gezle.model.Post

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    //For Fİrebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    private lateinit var postArrayList : ArrayList<Post>

    //For Adapter
    private lateinit var mainRecyclerAdapter : MainRecyclerAdapter
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

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerAdapter = MainRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = mainRecyclerAdapter

    }
    private fun getData(){
        db.collection("Posts").orderBy("time", Query.Direction.DESCENDING).addSnapshotListener { value, error -> //Postları al ve en son en üstte olsun.
            if(error != null){
                Toast.makeText(this, error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value != null){
                    if(!value.isEmpty){

                        val documents = value.documents

                        postArrayList.clear() //Diziyi temizlemek için

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

                        mainRecyclerAdapter.notifyDataSetChanged() //Veriler güncellendi ve yenilen anlamına gelir.

                    }
                }
            }
        }
    }

    fun show_location(view : View){

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //Menunyu aktiviteyle bagla
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.place_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Menuden eleman secilirse ne olacak
        if(item.itemId == R.id.add_place){
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        if(item.itemId == R.id.sign_out){
            auth.signOut()
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
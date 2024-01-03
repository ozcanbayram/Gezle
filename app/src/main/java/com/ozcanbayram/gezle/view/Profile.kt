package com.ozcanbayram.gezle.view

import ProfilePost
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.ozcanbayram.gezle.R
import com.ozcanbayram.gezle.adapter.MainRecyclerAdapter
import com.ozcanbayram.gezle.adapter.ProfileRecyclerAdapter
import com.ozcanbayram.gezle.databinding.ActivityLoginBinding
import com.ozcanbayram.gezle.databinding.ActivityProfileBinding
import com.ozcanbayram.gezle.model.Post


class Profile : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var profilePostArrayList : ArrayList<ProfilePost>
    private lateinit var profileRecyclerAdapter: ProfileRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /*val intentForName = intent
        var ad_soyad = intentForName.getStringExtra("ad_soyad")*/

        db = Firebase.firestore
        auth = Firebase.auth

        profilePostArrayList = ArrayList<ProfilePost>()

        getData()

        binding.recyclerViewForProfile.layoutManager = LinearLayoutManager(this)
        profileRecyclerAdapter = ProfileRecyclerAdapter(profilePostArrayList)
        binding.recyclerViewForProfile.adapter = profileRecyclerAdapter

    }

    private fun getData() {


        val currentsuer = auth.currentUser
        if (currentsuer != null) {
            val email = currentsuer.email

            val query = db.collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .whereEqualTo("email", email)

            query.addSnapshotListener { value, error -> //Postları al ve en son en üstte olsun.
                    if (error != null) {
                        //Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
                    } else {
                        if (value != null) {
                            if (!value.isEmpty) {

                                val documents = value.documents

                                profilePostArrayList.clear() //Diziyi temizlemek için

                                for (document in documents) {
                                    val id = document.id //Kimliği al
                                    val downloadUrl = document.get("downloadUrl") as String
                                    val email = document.get("email") as String
                                    val ad_soyad = document.get("ad_soyad") as String
                                    val comment = document.get("comment") as String
                                    val latitudeInfo = document.get("latitudeInfo") as String
                                    val longitudeInfo = document.get("longitudeInfo") as String
                                    val place_name = document.get("place_name") as String

                                    val post = ProfilePost(
                                        id ,
                                        downloadUrl,
                                        email,
                                        ad_soyad,
                                        comment,
                                        latitudeInfo,
                                        longitudeInfo,
                                        place_name
                                    )
                                    profilePostArrayList.add(post)

                                    //binding.textView.text = downloadUrl + email + ad_soyad + "\n" + comment + latitudeInfo + longitudeInfo + place_name

                                }

                                profileRecyclerAdapter.notifyDataSetChanged() //Veriler güncellendi ve yenilen anlamına gelir.
                            }
                        }
                    }
                }
        }
    }
}
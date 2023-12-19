package com.ozcanbayram.gezle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ozcanbayram.gezle.databinding.ActivityRegisterBinding
import com.ozcanbayram.gezle.databinding.ActivityWelcomeBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    //For Fİrebase Sign Up
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // Initialize Firebase Auth
        auth = Firebase.auth

    }
    fun register(view : View){
        val email  = binding.email.text.toString()
        val password = binding.pasword.text.toString()
        val password_control = binding.pasword2.text.toString()
        val ad_soyad = binding.adSoyad.text.toString()

        if(email.equals("") || password.equals("")){ //Boş bilgi kontrolü
            Toast.makeText(this,"Lütfen E-posta ve Şifre bilgilerinizi giriniz.",Toast.LENGTH_LONG).show()
        }
        else if (password != password_control){ //Parola tekrar kontrolü
            Toast.makeText(this,"Parolalar eşleşmiyor.",Toast.LENGTH_SHORT).show()
        }
        else{
            //Successfull
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                val intent = Intent(this@Register,Login::class.java)
                Toast.makeText(this@Register,"Harika! Gezlemek için giriş yapabilirsin.",Toast.LENGTH_LONG).show()
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //Fail
                Toast.makeText(this@Register,it.localizedMessage,Toast.LENGTH_LONG).show() //Başarısız olursa mesaj göster
            }
        }


    }
}
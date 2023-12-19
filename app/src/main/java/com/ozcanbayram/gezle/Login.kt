package com.ozcanbayram.gezle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ozcanbayram.gezle.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ozcanbayram.gezle.databinding.ActivityWelcomeBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    //For Fİrebase Sign Up
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun giris(view : View){
        val email = binding.adSoyad.text.toString()
        val password = binding.pasword.text.toString()

        if(email.equals("")||password.equals("")){
            Toast.makeText(this,"Lütfen E-posta ve Şifre bilgilerinizi giriniz.",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@Login,MainActivity::class.java)
                Toast.makeText(this@Login,"Giriş başarılı.",Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@Login,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

}
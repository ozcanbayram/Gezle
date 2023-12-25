package com.ozcanbayram.gezle.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.ozcanbayram.gezle.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun register(view: View) {
        val email = binding.email.text.toString()
        val password = binding.pasword.text.toString()
        val password_control = binding.pasword2.text.toString()
        val ad_soyad = binding.adSoyad.text.toString()

        if (email.isEmpty() || password.isEmpty()) { // Boş bilgi kontrolü
            Toast.makeText(this, "Lütfen E-posta ve Şifre bilgilerinizi giriniz.", Toast.LENGTH_LONG).show()
        } else if (password != password_control) { // Parola tekrar kontrolü
            Toast.makeText(this, "Parolalar eşleşmiyor.", Toast.LENGTH_SHORT).show()
        } else {
            // Başarılı
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    // Yeni kullanıcı başarıyla oluşturuldu
                    val user = authResult.user

                    // Kullanıcının adını ve soyadını ekleyin
                    val profileUpdates = userProfileChangeRequest {
                        displayName = ad_soyad
                    }

                    user?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                        if (profileTask.isSuccessful) {
                            // Kullanıcı adı ve soyadı eklendi
                            val intent = Intent(this@Register, Login::class.java)
                            Toast.makeText(
                                this@Register,
                                "Harika! Gezlemek için giriş yapabilirsin.",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intent)
                            finish()
                        } else {
                            // Kullanıcı profili güncellenirken bir hata oluşursa
                            Toast.makeText(
                                this@Register,
                                "Kullanıcı profili güncellenirken bir hata oluştu.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Başarısız olursa
                    Toast.makeText(this@Register, exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }

}

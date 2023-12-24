package com.ozcanbayram.gezle

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.ozcanbayram.gezle.databinding.ActivityDetailsBinding
import com.ozcanbayram.gezle.databinding.ActivityMapsBinding
import java.util.UUID

class Details : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    //izin istemek için:
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>

    //For Firebase
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    //For take latitude and longitude from DetailsActivity
    var latitudeInfo : String? = null
    var longitudeInfo : String? = null
    var placeName : String? = null

    var selectedPicture : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //MapsActivity'den gelen yer ismini textView5 e yazdırmak:
        val intentForPlaceName = intent
        val intentForLatitude = intent
        val intentForLongitude = intent
        val place_name =intentForPlaceName.getStringExtra("place_name")
        latitudeInfo = intentForLatitude.getStringExtra("enlem")
        longitudeInfo = intentForLongitude.getStringExtra("boylam")
        binding.textView5.text = place_name.toString()
        placeName = intentForPlaceName.getStringExtra("place_name")

        registerLauncher()

        //For Firebase
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage

    }

    fun select_image(view : View){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Görsel eklemek için galeri izni gerekli.",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        //Request Permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }else{
                    //Request Permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }else{
                //Permission Granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI) //Galeriden fotoğraf almak için
                // Start activity for result
                activityResultLauncher.launch(intentToGallery)
            }

        }else{

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Görsel eklemek için galeri izni gerekli.",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver"){
                        //Request Permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }else{
                    //Request Permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }else{
                //Permission Granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI) //Galeriden fotoğraf almak için
                // Start activity for result
                activityResultLauncher.launch(intentToGallery)
            }
        }

    }
    private fun registerLauncher(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result -> //Fotoğrafı almak için
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult  != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let {
                        binding.imageView8.setImageURI(it)
                    }
                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //Permission Granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI) //Galeriden fotoğraf almak için
                activityResultLauncher.launch(intentToGallery)
            }else{
                //Permission Denied
                Toast.makeText(this@Details,"Galeriden fotoğraf eklemek için iznine ihityaç var.",Toast.LENGTH_LONG).show()
            }
        }
    }


    fun share(view : View){

        //universal unique id (Random isimler üretmek için)
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReferance = reference.child("images").child(imageName)

        if(selectedPicture != null){
            imageReferance.putFile(selectedPicture!!).addOnSuccessListener {
                //Download Url -> Firestore (Burada paylaşım yapan kişinin verilreini Firestore'a ekleriz.)

                val uploadPictureReferance = storage.reference.child("images").child(imageName) //Yüklenen fotoğrafı indirmek için bir referans.
                uploadPictureReferance.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString() //image'i indirdi ve stringe çevirdi
                    if(auth.currentUser != null){
                        val postMap = HashMap<String, Any>() //Post bilgilerini bir hashmap içinde tutalım.
                        postMap.put("downloadUrl",downloadUrl)
                        postMap.put("email",auth.currentUser!!.email!!)
                        //buraya isim soyisim de eklenecek ****
                        postMap.put("ad_soyad",auth.currentUser!!.displayName.toString())
                        postMap.put("comment",binding.aciklama.text.toString())
                        postMap.put("latitudeInfo",latitudeInfo.toString())
                        postMap.put("longitudeInfo",longitudeInfo.toString())
                        postMap.put("place_name",placeName.toString())
                        postMap.put("time",Timestamp.now())

                        //verileri aldık ve şimdi veritananına koyalım:

                        firestore.collection("Posts").add(postMap).addOnSuccessListener {

                            finish()

                        }.addOnFailureListener {
                            Toast.makeText(this@Details,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(this@Details,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

}
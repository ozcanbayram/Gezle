package com.ozcanbayram.gezle.view

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ozcanbayram.gezle.R
import com.ozcanbayram.gezle.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    //İnit the LocationManager and LocationListener
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener
    //For Firebase
    private lateinit var auth: FirebaseAuth

    //For give permission:
    private lateinit var permissionLauncher : ActivityResultLauncher<String>

    //SharedPreferences
    private lateinit var sharedPrefereneces : SharedPreferences

    //onLocationChanged 1 kez çalrılması için:
    private var trackBoolean : Boolean? = null

    //Uzun tıklanarak seçilen enlem ve boylamı kaydetmek için değişken oluşturmak:
    private var selectedLatitude : Double? = null
    private var selectedLongitude : Double? = null

    private var latFromMain : String? = null
    private var longFromMain : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        registerLauncehr()

        sharedPrefereneces = this.getSharedPreferences("com.ozcanbayram.gezle", MODE_PRIVATE)
        trackBoolean = false
        selectedLatitude=0.0
        selectedLongitude=0.0

        binding.ilerle.setOnClickListener {

            if(binding.yerIsmi.text.isEmpty() || selectedLongitude ==0.0 || selectedLatitude == 0.0){
                Toast.makeText(this,"Gezlemek için lütfen konum seçin ve konumun adını girin.",Toast.LENGTH_LONG).show()
            }
            else{
                val intent = Intent(this, Details::class.java)
                intent.putExtra("place_name",binding.yerIsmi.text.toString()) //Girilen yer ismini detaylar sayfasına göndermek.
                intent.putExtra("enlem",selectedLatitude.toString())
                intent.putExtra("boylam",selectedLongitude.toString())
                startActivity(intent)
            }
        }

        val intetn = intent
        val info = intent.getStringExtra("info")
        if(info.equals("new")){
            //New post / coming from MainActivity
        }else{
            //Old post /coming from anyPostsButton (showLocation)
            binding.ilerle.visibility = View.GONE
            binding.yerIsmi.visibility = View.GONE
            // Yer ismini yazdır --> binding.textView4.text=
            //Go to location and add a marker
            val placeNameFromMain = intetn.getStringExtra("place")
            binding.textView4.text = placeNameFromMain
            latFromMain = intent.getStringExtra("lat")
            longFromMain = intent.getStringExtra("long")
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean { //Menunyu aktiviteyle bagla
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_for_maps,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean { //Menuden eleman secilirse ne olacak
        if(item.itemId == R.id.main){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        if(item.itemId == R.id.sign_out){
            auth.signOut()
            val intent = Intent(this, FirstActivity::class.java)
            startActivity(intent)
            finish()
        }
        if(item.itemId == R.id.profile){
            val intent = Intent(this,Profile::class.java)
            //intent.putExtra("ad_soyad",ad_soyad)
            startActivity(intent)
        }
        if(item.itemId == R.id.notification_permission){
            val intent = Intent()
            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) { //Harita hazı olduğunda çalışacak metot.
        mMap = googleMap
        mMap.setOnMapLongClickListener(this) //Harita ile uzun tıklama OnMapLongClickListener arasındaki bağlantı.

        val intetn = intent
        val info = intent.getStringExtra("info")
        if(info.equals("old")){
            //Old post / coming from MainActivity
            val placeNameFromMain = intetn.getStringExtra("place")
            val oldLocation = LatLng(latFromMain!!.toDouble(),longFromMain!!.toDouble()) as LatLng
            mMap.addMarker(MarkerOptions().position(oldLocation).title(placeNameFromMain))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oldLocation,15f))


        }else {

            //LocationManager tanımlama. getSystemService ile androidin sistem servislerine ulaşabiliriz.
            locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager//Casting işlemi (as LocationManager ile döndürülecek şeyden emin olduğumuzu belirttik.
            //locationListener kullanma
            locationListener = object : LocationListener { //Oluşturulan obje üzerinden işlemler yapılır. gerekli ögeler iplement edilmelidir.
                override fun onLocationChanged(location: Location) { //Konum değiştiği zaman bize verilecek location(içerisinde Location tutan parametre)
                    //Bu metodu tanımladıktan sonra kullanabilmek için kullanıcıdan konum izni almalıyız.
                    //Need Permission

                    trackBoolean = sharedPrefereneces.getBoolean("trackBoolean",false)
                    if(!trackBoolean!!){
                        val userLocation = LatLng(location.latitude,location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f)) //sürekli onLocationChanged çağrılırsa haritada rahat gezemeyiz
                        //bu yüzden sharedPreferences kullanarak bunun sadece 1 kez çağrılmasını sağlayabiliriz.
                        sharedPrefereneces.edit().putBoolean("trackBoolean",true).apply() //TrackBoolean artık true olur ve bir daha çalışmaz
                    }


                }





                //Bazı cihazlardaki bugları engellemek için aşağıdaki metodu genellikle tanımlamamızda fayda vardır.
                /*override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    super.onStatusChanged(provider, status, extras)
                }*/
            }



            //İzin Kontrol: Check Permission:
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //Permission Denied (Request permission)
                //Kullanıcıdan izin alınmalıdır. Neden izin istediğimizi söylemeli ve izni istemeliyiz.
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(binding.root,"Permission needed for location and use the GEZLE application",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give Permission"){
                            //Request Permission   (izin istemek için ActivityResultLauncher kullanılır)
                            //İzin istemek için aşağıda oluşturduğumuz permissionlauncher'i kullanırız:
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }.show()
                }else{
                    //Request Permission
                    //Request Permission   (izin istemek için ActivityResultLauncher kullanılır)
                    //İzin istemek için aşağıda oluşturduğumuz permissionlauncher'i kullanırız:
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }else{
                //Permission Granted

                //Konum güncellemelerini alalım:
                //Kod amaçları: [locationManager: tanımlanan konum yöneticisini çağır].[requestLocationUpdate: Konum güncellemelerini al]([LocationManager:
                // konum yöneticisi sınıfı ile provider'i bellirt. yani konumu hangi sağlayıcıyla alacağını belirt].[GPS_PROVIDER:Konumu GPS sağlayıcısından al]
                // kaç saniyede bir  konum güncellemesini alacağını milisaniy cinsinden belirt, kaç metreede bir alınacağını float cinsinden belirt.,
                // [locationListener: buradan gelen verileri global tanımlı konum dinleyicisine ata(Yuakrıda kullanılan locationListener)]) -->
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener) //needing location permission

                //Kullanıcının son konumunu alma
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastLocation != null){
                    val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                }
                mMap.isMyLocationEnabled = true //Konumum alındı mı doğrulaması
            }

        }







        /*
        //Learning how working the Maps Activity (Add location, add marker, move camera, zoom)
        //Eiffel Tower --> latitude: 48.85838581515857,  longitude: 2.2945080249890766
        val eiffel = LatLng(48.85838581515857, 2.2945080249890766) //İnitilazed a latitude and longitude (location)
        mMap.addMarker(MarkerOptions().position(eiffel).title("Eiffel Tower")) // For add to marker to our position (location)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,15f)) //For move the maps camera and zoom
        */
    }


    private fun registerLauncehr(){ //Bu fonksiyon onCreate altında kesinlikle çağrılmalıdır. İzin isteme işlemi burada ve yukarıda tanımlanan
                                    //private lateinit var permissionLauncher : ActivityResultLauncher<String>  ile gerçekleştirilir.
        //permissionLauncher'in ne olacağını tanımlayalım:
        //'registerForActivityResult()' hazır metodu kullanılır ve bu metodun içerisinde ne yapacağımız belirtilir. İsteiğimiz işlemi sınıf olarak yazabiliriz.
        //Örneğin RequestPermission()
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->  //Burada result boolean bir değerdir.
            //resulttan gelen boolean değeri true/false olarak kontrol edilir.
            if (result == true){
                //Konum iznini tekarar kontrol edelim ve izin verildiyse konum güncellemelerini tekrardan alalım.
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //Permission Granted
                    //Burada izin verilmiştir ve tekrardan konum güncellemesini almaya hazırız demektir.
                    //Konum güncellemelerini alalım:
                    //Kod amaçları: [locationManager: tanımlanan konum yöneticisini çağır].[requestLocationUpdate: Konum güncellemelerini al]([LocationManager:
                    // konum yöneticisi sınıfı ile provider'i bellirt. yani konumu hangi sağlayıcıyla alacağını belirt].[GPS_PROVIDER:Konumu GPS sağlayıcısından al]
                    // kaç saniyede bir  konum güncellemesini alacağını milisaniy cinsinden belirt, kaç metreede bir alınacağını float cinsinden belirt.,
                    // [locationListener: buradan gelen verileri global tanımlı konum dinleyicisine ata(Yuakrıda kullanılan locationListener)]) -->
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,locationListener) //needing location permission

                    //Kullanıcının son konumunu alma
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastLocation != null){
                        val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                    }
                    mMap.isMyLocationEnabled = true //Konumum alındı mı doğrulaması
                }

            }else{
                //Permission Denied
                Toast.makeText(this@MapsActivity,"Permission needed for use your location and Gezle application", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onMapLongClick(p0: LatLng) {  //Yukarıda eklenen GoogleMap.OnMapLongClickListener eklentisi
        mMap.clear() //önceden eklenen markerları vb.. temizle
        mMap.addMarker(MarkerOptions().position(p0)) //Uzun tıklanan yere marker ekle

        //Global tanımlı olan değişkenlere uzun tıklayarak kaydetmek istediğimiz konumun enlem boylamını verelim:
        selectedLatitude = p0.latitude
        selectedLongitude = p0.longitude


    }


}
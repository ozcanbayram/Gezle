package com.ozcanbayram.gezle

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ozcanbayram.gezle.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    //İnit the LocationManager and LocationListener
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) { //Harita hazı olduğunda çalışacak metot.
        mMap = googleMap


        //LocationManager tanımlama. getSystemService ile androidin sistem servislerine ulaşabiliriz.
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager//Casting işlemi (as LocationManager ile döndürülecek şeyden emin olduğumuzu belirttik.
        //locationListener kullanma
        locationListener = object : LocationListener { //Oluşturulan obje üzerinden işlemler yapılır. gerekli ögeler iplement edilmelidir.
            override fun onLocationChanged(p0: Location) { //Konum değiştiği zaman bize verilecek p0(içerisinde Location tutan parametre)
                //Bu metodu tanımladıktan sonra kullanabilmek için kullanıcıdan konum izni almalıyız.
            }











            //Bazı cihazlardaki bugları engellemek için aşağıdaki metodu genellikle tanımlamamızda fayda vardır.
            /*override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                super.onStatusChanged(provider, status, extras)
            }*/
        }



        //Konum güncellemelerini alalım:
        //Kod amaçları: [locationManager: tanımlanan konum yöneticisini çağır].[requestLocationUpdate: Konum güncellemelerini al]([LocationManager:
        // konum yöneticisi sınıfı ile provider'i bellirt. yani konumu hangi sağlayıcıyla alacağını belirt].[GPS_PROVIDER:Konumu GPS sağlayıcısından al]
        // kaç saniyede bir  konum güncellemesini alacağını milisaniy cinsinden belirt, kaç metreede bir alınacağını float cinsinden belirt.,
        // [locationListener: buradan gelen verileri global tanımlı konum dinleyicisine ata(Yuakrıda kullanılan locationListener)])
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,locationListener) //needing location permission









        /*
        //Learning how working the Maps Activity (Add location, add marker, move camera, zoom)
        //Eiffel Tower --> latitude: 48.85838581515857,  longitude: 2.2945080249890766
        val eiffel = LatLng(48.85838581515857, 2.2945080249890766) //İnitilazed a latitude and longitude (location)
        mMap.addMarker(MarkerOptions().position(eiffel).title("Eiffel Tower")) // For add to marker to our position (location)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,15f)) //For move the maps camera and zoom
        */
    }
}
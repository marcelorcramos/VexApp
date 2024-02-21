package com.example.vexaplicao2

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.vexaplicao2.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastlocation: Location
    private lateinit var binding: ActivityMapsBinding
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode =101

    private val places = arrayListOf(
        Place("Pingo Doce Carlos Mardel", LatLng(38.7340809,-9.133378),"R. Carlos Mardel 12","1900-122","CCS2 & CHAdeMO","NIVEL 3"),
        Place("Hotel Travel Park Lisboa", LatLng(38.7271382,-9.1373133),"Av. Alm. Reis 64","1150-020", "CHAdeMO & CCS2","NIVEL 3"),
        Place("Hotel Travel Park Lisboa", LatLng(38.7271382,-9.1373133),"Av. Alm. Reis 64","1150-020", "TYPE2","NIVEL 2"),
        Place("Alameda Dom Afonso Henriques", LatLng(38.7374043,-9.1351184),"Alameda Dom Afonso Henriques","1000-125", "TYPE2","NIVEL 2"),
        Place("R. Alves Redol", LatLng(38.7365007,-9.1414391),"R. Alves Redol","1000-029", "TYPE 2 ","NIVEL 2"),
        Place("KLC Charging Station", LatLng(38.7379657,-9.1405744),"R. Alves Redol 7C","1049-001", "TYPE 2","NIVEL 1"),
        Place("Av. de António José de Almeida", LatLng(38.7382815,-9.1413488),"Av. de António José de Almeida 28A","1000-021", "TYPE 2","NIVEL 2"),
        Place("Praça de Londres", LatLng(38.7396857,-9.1376345),"Parque Praça de Londres Telpark by Empark","1000-000", "TYPE 2 ","NIVEL 2"),
        Place("Praça de Londres", LatLng(38.7401948,-9.1368325),"Praça de Londres 10 B","1000-192", "TYPE 2 ","NIVEL 2"),
        Place("Instituto Superior Tecnico", LatLng(38.737216, -9.139005),"Av. Rovisco Pais 1","1049-001", "TYPE 2 ","NIVEL 2"),
        Place("Edificio Atrium Saldanha", LatLng(38.7329841,-9.1454947),"Praça Duque de Saldanha 1","1050-094", "TYPE 2 ","NIVEL 2"),
        Place("Edificio Atrium Saldanha ", LatLng(38.7329841,-9.1454947),"Praça Duque de Saldanha 1","1050-094", "CCS2 & CHAdeMO & TYPE2","NIVEL 4"),
        Place("Empark Saldanha Residence", LatLng(38.7317411,-9.1472002),"Av. Fontes Pereira de Melo","1050-000", "TYPE 2 ","NIVEL 2"),
        Place("Parque Estacionamento Saldanha", LatLng(38.7319167,-9.1472255),"R. Latino Coelho,Lisboa","1050-135", "TYPE 2 ","NIVEL 2"),
        Place("Parque Arco do Cego - Subterrâneo Telpark by Empark", LatLng(38.7363883,-9.1436078),"Av. João Crisóstomo","1000-178", "TYPE 2 ","NIVEL 2"),
        Place("MOBI.E Charging Station", LatLng(38.7371572,-9.1280937),"R. Cristóvão Falcão 26","1900-167", "TYPE 2 ","NIVEL 1"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(){googleMap ->
            googleMap.setInfoWindowAdapter(MarkerInfoAdapter(this))
            addMarkers(googleMap)


            getCurrentLocationUser()

            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()

                places.forEach(){
                    bounds.include(it.latLng)
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(),200))
            }
        }
    }

    private fun getCurrentLocationUser(){
        if(ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),permissionCode)
            return

        }

        val getLocation= fusedLocationProviderClient.lastLocation.addOnSuccessListener {

                location ->

            if(location != null){
                currentLocation =location

                Toast.makeText(applicationContext, currentLocation.latitude.toString()+""+
                        currentLocation.longitude.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0]==
                PackageManager.PERMISSION_GRANTED){
                getCurrentLocationUser()
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap){
        places.forEach() { place ->
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .snippet(place.address)
                    .position(place.latLng)
                    .snippet(place.cp)
            )
            marker?.tag = place
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()

        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("Localização atual")

        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,7f))
        googleMap?.addMarker(markerOptions)
    }

    private fun setUpMap(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if(location != null){
                lastlocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLng: LatLng){
        val markerOptions = MarkerOptions()
    }
}

data class Places(
    val name : String,
    val latLng: LatLng,
    val address: String,
    val cp: String,
    val tipo : String,
    val nivel : String
)

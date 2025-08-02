
package com.example.campusbite

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapPickerActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_picker)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val defaultLocation = LatLng(23.8103, 90.4125) // Dhaka
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        map.setOnMapClickListener { latLng ->
            map.clear()
            map.addMarker(MarkerOptions().position(latLng).title("Selected Location"))

            val geocoder = Geocoder(this, Locale.getDefault())
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            val locationName = address?.get(0)?.getAddressLine(0) ?: "Unknown Location"

            val resultIntent = Intent()
            resultIntent.putExtra("location", locationName)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
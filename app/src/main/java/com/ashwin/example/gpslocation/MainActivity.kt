package com.ashwin.example.gpslocation

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Build
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import android.content.Intent
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    private val LOCATION_REQUEST_CODE: Int = 10

    private var mLocationManager: LocationManager? = null
    private var mLocationListener: LocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentLocationTextView.setText("Latitude: " + location.latitude + ", Longitude: " + location.longitude);
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
                // Do nothing
            }

            override fun onProviderEnabled(provider: String) {
                // Do nothing
            }

            override fun onProviderDisabled(provider: String) {
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(i)
            }
        }

        currentLocationButton.setOnClickListener {
            updateLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> updateLocation()
            else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLocation() {
        // First check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET), LOCATION_REQUEST_CODE)
            }
            return
        }

        // Get location
        mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, mLocationListener)
    }
}

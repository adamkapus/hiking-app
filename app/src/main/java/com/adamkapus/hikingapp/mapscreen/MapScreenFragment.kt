package com.adamkapus.hikingapp.mapscreen

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentMapScreenBinding
import com.adamkapus.hikingapp.utils.PermissionUtils.isPermissionGranted
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.InputStreamReader

class MapScreenFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapScreenBinding

    private var map: GoogleMap? = null
    private var permissionDenied = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        const val PICK_GPX_FILE = 101

        private const val DEFAULT_LOCATION_LAT = 47.4733057775952
        private const val DEFAULT_LOCATION_LNG = 19.059724793021967
    }

    private val startForGPXFileResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.data?.also { uri ->
                    openGPXFile(uri)
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.test.setOnClickListener {
            askForGpxFile()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val defaultLocation = LatLng(DEFAULT_LOCATION_LAT, DEFAULT_LOCATION_LNG)
        map!!.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        enableMyLocation()
    }

    private fun askForGpxFile() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/gpx+xml"
        }
        startForGPXFileResult.launch(intent)
    }

    private fun openGPXFile(uri: Uri) {
        Log.d("PLS", uri.toString())
        val contentResolver = context?.contentResolver
        if(contentResolver != null) {
            contentResolver.openInputStream(uri)?.use { inputStream ->

            }
        }

    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map!!.isMyLocationEnabled = true
            return
        }

        // If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                ACCESS_COARSE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

}
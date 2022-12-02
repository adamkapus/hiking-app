package com.adamkapus.hikingapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentMapScreenBinding
import com.adamkapus.hikingapp.utils.MapUtils
import com.adamkapus.hikingapp.utils.PermissionUtils.hasLocationPermission
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapScreenBinding

    private val viewModel: MapViewModel by viewModels()

    private var map: GoogleMap? = null
    private var permissionDenied = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        private val REQUIRED_PERMISSIONS =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )


        private const val DEFAULT_LOCATION_LAT = 47.4733057775952
        private const val DEFAULT_LOCATION_LNG = 19.059724793021967

        private const val TAG = "PLS"
    }

    private val gpxFileRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                intent?.data?.also { uri ->
                    //openGPXFile(uri)
                    gpxFileSelected(uri)
                }
            }
        }


    private val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                tryLoadingUserPosition()
            } else if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                tryLoadingUserPosition()
            } else {
                viewModel.onLocationPermissionsDenied()
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

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
    }

    private fun render(uiState: MapUiState) {
        when (uiState) {
            is MapUiState.Initial -> {
                Log.d("PLS", uiState.toString())
            }
            is MapUiState.RouteLoaded -> {
                Log.d("PLS", uiState.toString())
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        //val defaultLocation = LatLng(DEFAULT_LOCATION_LAT, DEFAULT_LOCATION_LNG)
        //map!!.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        tryLoadingUserPosition()
        //enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun tryLoadingUserPosition() {
        if (requireContext().hasLocationPermission()
        ) {
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
            viewModel.loadUserPosition()
        } else {
            locationPermissionRequest.launch(
                REQUIRED_PERMISSIONS
            )
        }
    }

    private fun askForGpxFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/gpx+xml"
        }
        gpxFileRequest.launch(intent)
    }

    private fun gpxFileSelected(uri: Uri) {
        val contentResolver = context?.contentResolver
        if (contentResolver != null) {
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                viewModel.loadGpxFile(inputStream)
            }
        }
    }

    /*
    private fun openGPXFile(uri: Uri) {
        Log.d("TAG", uri.toString())
        val contentResolver = context?.contentResolver
        if (contentResolver != null) {
            val interactor = GpxInteractor()
            val inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val route = interactor.parseFile(inputStream)
                drawRouteOnMap(route)
            }

            /*contentResolver.openInputStream(uri)?.use { inputStream ->
                val mParser = GPXParser()
                var parsedGpx: Gpx? = null
                try {
                    parsedGpx =
                        mParser.parse(inputStream) // consider doing this on a background thread
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: XmlPullParserException) {
                    e.printStackTrace()
                }

                val route: MutableList<LatLng> = mutableListOf()
                if (parsedGpx != null) {
                    // log stuff
                    val tracks: List<Track> = parsedGpx.tracks
                    for (i in tracks.indices) {
                        val track: Track = tracks[i]
                        Log.d(TAG, "track $i:")
                        val segments: List<TrackSegment> = track.getTrackSegments()
                        for (j in segments.indices) {
                            val segment = segments[j]
                            Log.d(TAG, "  segment $j:")
                            for (trackPoint in segment.trackPoints) {
                                route.add(LatLng(trackPoint.latitude, trackPoint.longitude))
                                var msg =
                                    "    point: lat " + trackPoint.latitude + ", lon " + trackPoint.longitude + ", time " + trackPoint.time
                                val ext: Extensions? = trackPoint.extensions
                                var speed: Double
                                if (ext != null) {
                                    speed = ext.getSpeed()
                                    msg = "$msg, speed $speed"
                                }
                                Log.d(TAG, msg)
                            }
                        }
                    }

                } else {
                    Log.e(TAG, "Error parsing gpx track!")
                }
                drawRouteOnMap(route)
            }*/
        }

    }
    */

    private fun drawRouteOnMap(trackingPointList: List<LatLng>) {
        if (map != null) {
            //val route = Route(trackingPointList)
            MapUtils.addRouteToMap(requireContext(), map!!, trackingPointList)
        }
    }

    /*
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
    }*/

}
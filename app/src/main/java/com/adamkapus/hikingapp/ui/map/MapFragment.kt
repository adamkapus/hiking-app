package com.adamkapus.hikingapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentMapScreenBinding
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.ui.map.model.FlowerOnMap
import com.adamkapus.hikingapp.utils.FlowerRarity
import com.adamkapus.hikingapp.utils.MapUtils
import com.adamkapus.hikingapp.utils.hasLocationPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private lateinit var binding: FragmentMapScreenBinding

    private val viewModel: MapViewModel by viewModels()

    private var map: GoogleMap? = null
    private var markers = HashMap<Marker, FlowerOnMap>()
    private var userRouteId: Int? = null

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

        userRouteId = try {
            arguments?.getString("userRouteId")
                ?.toInt()
        } catch (e: java.lang.NumberFormatException) {
            null
        }

        Log.d("PLS", "MAPBEN A ROUITE ID: " + userRouteId.toString())


        setupButtons()
        setupCheckboxes()

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }

        viewModel.loadFlowerLocations()
    }

    private fun render(uiState: MapUiState) {
        when (uiState) {
            is MapUiState.Initial -> {
                Log.d("PLS", uiState.toString())
                drawMarkersOnMap(uiState.flowers)
                val location = uiState.userPosition?.let { LatLng(it.latitude, it.longitude) }
                moveCamera(location)
            }
            is MapUiState.RouteLoaded -> {
                Log.d("PLS", uiState.toString())
                drawMarkersOnMap(uiState.flowers)
                drawRouteOnMap(uiState.route)
                val location = uiState.route.points.firstOrNull()?.let { LatLng(it.Lat, it.Lng) }
                moveCamera(location)
            }
        }
    }

    private fun setupButtons() {
        binding.test.setOnClickListener {
            askForGpxFile()
        }

    }

    private fun moveCamera(location: LatLng?) {
        if (location == null || map == null) {
            return
        }
        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f))
    }

    private fun setupCheckboxes() {
        binding.checkboxCommon.setOnClickListener { view ->
            if (view is CheckBox) onCheckBoxClick(
                FlowerRarity.COMMON,
                view.isChecked
            )
        }
        binding.checkboxRare.setOnClickListener { view ->
            if (view is CheckBox) onCheckBoxClick(
                FlowerRarity.RARE,
                view.isChecked
            )
        }
        binding.checkboxSuperRare.setOnClickListener { view ->
            if (view is CheckBox) onCheckBoxClick(
                FlowerRarity.SUPER_RARE,
                view.isChecked
            )
        }
    }

    private fun onCheckBoxClick(rarity: FlowerRarity, isChecked: Boolean) {
        viewModel.onRarityVisibilityChanged(rarity, isChecked)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        googleMap.setInfoWindowAdapter(FlowerInfoWindowAdapter())
        googleMap.setOnInfoWindowClickListener(this)

        tryLoadingUserPosition()
        if (userRouteId != null) {
            viewModel.loadUserRecordedRoute(userRouteId!!)
        }
    }

    private fun drawMarkersOnMap(flowers: List<FlowerOnMap>) {
        if (map == null) {
            return
        }
        for (m in markers.keys) {
            m.remove()
        }
        //a [marker, FlowerOnMap] hashmap-et töröljük
        markers.clear()

        for (f in flowers) {
            val pos = LatLng(f.Lat!!.toDouble(), f.Lng!!.toDouble())
            val icon: Int = when (f.rarity) {
                FlowerRarity.COMMON -> R.drawable.ic_map_flower_common
                FlowerRarity.RARE -> R.drawable.ic_map_flower_rare
                FlowerRarity.SUPER_RARE -> R.drawable.ic_map_flower_super_rare
                else -> {
                    R.drawable.ic_map_flower_common
                }
            }

            val marker = map!!.addMarker(
                MarkerOptions().position(pos).title(f.name)
                    .icon(BitmapDescriptorFactory.fromResource(icon))
            )
            if (marker != null) {
                //betesszük a HashMap-be a markert és a FlowerOnMap-pé konvertált virágot
                markers[marker] = f
            }
        }

    }

    //InfoWindow adapter az egyedi InfoWindowokért a térképen
    internal inner class FlowerInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        private val window: View = layoutInflater.inflate(R.layout.flower_info_window, null)

        //ezt a függvényt akkor kéne megvalósítani, ha az infowindow tartalmát akarnánk csak felülírni, de mi a kinézetét is szeretnénk
        override fun getInfoContents(marker: Marker): View? {
            return null
        }

        override fun getInfoWindow(marker: Marker): View? {
            render(marker, window)
            return window
        }

        //beállítjuk az egyedi info window kinézetét
        private fun render(marker: Marker, view: View) {
            val title: String? = markers[marker]?.displayName
            val titleUi = view.findViewById<TextView>(R.id.title)
            titleUi.text = title

            val snippetUi = view.findViewById<TextView>(R.id.snippet)
            val lat = String.format("%.3f", markers[marker]?.Lat)
            val lng = String.format("%.3f", markers[marker]?.Lng)
            snippetUi.text = getString(R.string.lat_lng, lat, lng)

            val viewImagetv = view.findViewById<TextView>(R.id.view_image_tv)
            viewImagetv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        }

    }

    override fun onInfoWindowClick(marker: Marker) {
        val flowerName = markers[marker]?.displayName
        val imageUrl = markers[marker]?.imageUrl
        val action =
            MapFragmentDirections.actionMapFragmentToFlowerImageFragment(flowerName, imageUrl)
        findNavController().navigate(action)
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
                viewModel.loadRouteFromGpxFile(inputStream)
            }
        }
    }

    private fun drawRouteOnMap(route: Route) {
        if (map != null) {
            val trackingPointList = route.points.map { LatLng(it.Lat, it.Lng) }
            MapUtils.addRouteToMap(requireContext(), map!!, trackingPointList)
        }
    }
}
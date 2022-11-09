package com.adamkapus.hikingapp.ui.track

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentTrackScreenBinding
import com.adamkapus.hikingapp.ui.track.tracking.LocationService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

class TrackScreenFragment : Fragment(), OnMapReadyCallback {
    /*ToDo DI*/
    private val viewModel = TrackScreenViewModel()
    private lateinit var binding: FragmentTrackScreenBinding

    private lateinit var map: GoogleMap
    private var mapLoaded = false

    private lateinit var locationService: LocationService
    private var boundToService: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as LocationService.LocalBinder
            locationService = binder.getService()
            boundToService = true

            locationService.locationList.observe(viewLifecycleOwner) {
                viewModel.addRoute(it)
            }

        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            boundToService = false
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.start.setOnClickListener {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                requireActivity().startService(this)
            }
        }

        binding.stop.setOnClickListener {
            Intent(context, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                requireActivity().startService(this)
            }
        }

        binding.clear.setOnClickListener {
            viewModel.clearRoute()
        }

        viewModel.route.observe(viewLifecycleOwner) {
            drawRoute(it)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(context, LocationService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        Intent(context, LocationService::class.java).also { intent ->
            requireActivity().unbindService(connection)
        }
        boundToService = false
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        mapLoaded = true

        //ToDo check permissions
        map.isMyLocationEnabled = true


        val route: Polyline = map.addPolyline(
            PolylineOptions()

        )
        //route.points = routePoints
    }

    private fun drawRoute(route: List<LatLng>) {
        Log.d("PLS", "Draw" + route.toString())
        if (mapLoaded) {
            val polyline: Polyline = map.addPolyline(
                PolylineOptions()
                    //.width(40.0f)
                    .color(Color.RED)
            )
            polyline.points = route
        }
    }
}
package com.adamkapus.hikingapp.ui.track

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.databinding.FragmentTrackBinding
import com.adamkapus.hikingapp.ui.track.TrackUiState.*
import com.adamkapus.hikingapp.ui.track.tracking.TrackService
import com.adamkapus.hikingapp.utils.PermissionUtils.hasLocationPermission
import com.adamkapus.hikingapp.utils.showSnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TrackFragment : Fragment() {
    private val viewModel: TrackViewModel by viewModels()
    private lateinit var binding: FragmentTrackBinding
    private lateinit var startButton: Button
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var trackName: EditText

    companion object {
        private val REQUIRED_PERMISSIONS =
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
    }

    private val locationPermissionRequest =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                onStartButtonPressed()
            } else if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                onStartButtonPressed()
            } else {
                showSnackbar(R.string.permission_location_tracking_reasoning)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrackBinding.inflate(layoutInflater)
        startButton = binding.trackStartButton
        saveButton = binding.trackSaveButton
        cancelButton = binding.trackCancelButton
        trackName = binding.trackName
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startButton.setOnClickListener {
            onStartButtonPressed()
        }

        saveButton.setOnClickListener {
            onSaveButtonPressed()
        }

        cancelButton.setOnClickListener {
            onCancelButtonPressed()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
        viewModel.initUiState()
    }

    private fun render(uiState: TrackUiState) {
        when (uiState) {
            is Initial -> {
                startButton.isEnabled = false
                saveButton.isEnabled = false
                cancelButton.isEnabled = false
            }
            ReadyToStart -> {
                startButton.isEnabled = true
                saveButton.isEnabled = false
                cancelButton.isEnabled = false
            }
            TrackingInProgress -> {
                startButton.isEnabled = false
                saveButton.isEnabled = true
                cancelButton.isEnabled = true
            }
            SavingRouteFailed -> {
                showSnackbar(R.string.track_saving_route_failed)
                viewModel.handledTrackingFailed()
            }
            SavingRouteSuccess -> {
                showSnackbar(R.string.track_saving_route_success)
                viewModel.handledTrackingSuccess()
            }
            SavingRouteInProgress -> {

            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    private fun onStartButtonPressed() {
        if (context?.hasLocationPermission() == true) {
            startService()
            viewModel.trackingStarted()
        } else {
            locationPermissionRequest.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun onSaveButtonPressed() {
        stopService()
        val name = trackName.text.toString().ifEmpty {
            "Route"
        }
        viewModel.saveRoute(name)
    }

    private fun onCancelButtonPressed() {
        stopService()
        viewModel.cancelTracking()
    }

    private fun startService() {
        val intent = Intent(context, TrackService::class.java)
        intent.action = TrackService.ACTION_START
        activity?.startService(intent)
    }

    private fun stopService() {
        val intent = Intent(context, TrackService::class.java)
        intent.action = TrackService.ACTION_STOP
        activity?.startService(intent)
    }
}
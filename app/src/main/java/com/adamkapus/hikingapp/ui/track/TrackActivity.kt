package com.adamkapus.hikingapp.ui.track

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adamkapus.hikingapp.databinding.ActivityTrackBinding
import com.adamkapus.hikingapp.ui.track.tracking.LocationService
import com.adamkapus.hikingapp.ui.track.tracking.PlsService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/*
@AndroidEntryPoint
class TrackActivity : AppCompatActivity() {
    private val viewModel: TrackViewModel by viewModels()
    private lateinit var binding: ActivityTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startt.setOnClickListener {
            onStartButtonPressed()
        }

        binding.savet.setOnClickListener {
            onSaveButtonPressed()
        }

        binding.cancelt.setOnClickListener {
            onCancelButtonPressed()
        }

        lifecycleScope.launch {
            viewModel.uiState.collect {
                render(it)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        viewModel.initUiState()
    }

    private fun render(uiState: TrackUiState) {
        Log.d("PLS", uiState.toString())
        when (uiState) {
            is TrackUiState.Initial -> {
                binding.startt.isEnabled = false
                binding.savet.isEnabled = false
                binding.cancelt.isEnabled = false
            }
            TrackUiState.ReadyToStart -> {
                binding.startt.isEnabled = true
                binding.savet.isEnabled = false
                binding.cancelt.isEnabled = false
            }
            TrackUiState.TrackingInProgress -> {
                binding.startt.isEnabled = false
                binding.savet.isEnabled = true
                binding.cancelt.isEnabled = true
            }
            TrackUiState.SavingRouteFailed -> {}
            TrackUiState.SavingRouteInProgress -> {
                TODO()
            }
            TrackUiState.SavingRouteSuccess -> {
                TODO()
            }

        }
    }

    private fun onStartButtonPressed() {
        //start service
        /*val intent = Intent(applicationContext, PlsService::class.java)
        intent.action = PlsService.ACTION_START
        startService(intent)*/
        Intent(applicationContext, PlsService::class.java).apply {
            action = PlsService.ACTION_START
            startService(this)
        }
        /*Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            context?.startService(this)
        }*/
        viewModel.trackingStarted()
    }

    private fun onSaveButtonPressed() {
        //stop service
        /*val intent = Intent(applicationContext, PlsService::class.java)
        intent.action = PlsService.ACTION_STOP
        startService(intent)*/
        Intent(applicationContext, PlsService::class.java).apply {
            action = PlsService.ACTION_STOP
            startService(this)
        }
        /*Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            context?.startService(this)
        }*/
        viewModel.saveRoute()
    }

    private fun onCancelButtonPressed() {
        viewModel.cancelTracking()
    }
}*/
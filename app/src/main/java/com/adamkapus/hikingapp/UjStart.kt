package com.adamkapus.hikingapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.adamkapus.hikingapp.databinding.ActivityUjStartBinding
import com.adamkapus.hikingapp.ui.track.tracking.PlsService

/*
class UjStart : AppCompatActivity() {
    private lateinit var binding: ActivityUjStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUjStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            Intent(applicationContext, PlsService::class.java).apply {
                action = PlsService.ACTION_START
                startService(this)
            }
        }

        binding.stop.setOnClickListener {
            Intent(applicationContext, PlsService::class.java).apply {
                action = PlsService.ACTION_STOP
                startService(this)
            }
        }


        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
    }
}*/

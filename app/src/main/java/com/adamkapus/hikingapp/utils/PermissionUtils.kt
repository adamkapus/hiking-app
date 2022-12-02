package com.adamkapus.hikingapp.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {

    fun isPermissionGranted(
        grantPermissions: Array<String>, grantResults: IntArray, permission: String
    ): Boolean {
        for (i in grantPermissions.indices) {
            if (permission == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }

    fun Context.hasAllPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(
                this, it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun Context.hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun runCodeRequiringPermission(
        activity: AppCompatActivity,
        requiredPermissions: Array<String>,
        request: ActivityResultLauncher<String>,
        onPermissionGranted: () -> Unit,
        onShowRationale: () -> Unit
    ) {
        val perm = requiredPermissions[0]

        when {
            ContextCompat.checkSelfPermission(
                activity,
                perm
            ) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, perm) -> {
                onShowRationale()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                request.launch(
                    perm
                )
            }
        }


    }

}
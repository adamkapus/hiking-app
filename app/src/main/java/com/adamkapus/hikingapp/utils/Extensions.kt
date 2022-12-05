package com.adamkapus.hikingapp.utils

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun CharSequence?.isNotNullOrBlank() = !isNullOrBlank()

fun Fragment.showSnackbar(@StringRes resId: Int) {
    this.view?.let {
        Snackbar.make(it, resId, Snackbar.LENGTH_SHORT)
            .show()
    }
}
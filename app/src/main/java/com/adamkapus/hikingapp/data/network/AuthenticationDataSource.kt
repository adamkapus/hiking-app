package com.adamkapus.hikingapp.data.network

import android.util.Log
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticationDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {


    suspend fun signUp(
        emailAddress: String,
        password: String,
    ) = suspendCancellableCoroutine<DataSourceResponse<Boolean>> { continuation ->
        auth.createUserWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    continuation.resume(DataSourceResult(result = true)) {}
                } else {
                    continuation.resume(DataSourceResult(result = false)) {}
                }
            }
    }

    suspend fun signOut(
    ): DataSourceResponse<Boolean> {
        auth.signOut()
        return DataSourceResult(true)
    }

    suspend fun getUserInfo(): DataSourceResponse<HikingAppUser> {
        val user = auth.currentUser
        return if (user == null) {
            DataSourceError
        } else {
            DataSourceResult(HikingAppUser(email = user.email))
        }

    }

    suspend fun signIn(
        emailAddress: String,
        password: String,
    ) = suspendCancellableCoroutine<DataSourceResponse<Boolean>> { continuation ->
        auth.signInWithEmailAndPassword(emailAddress, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    continuation.resume(DataSourceResult(result = true)) {}
                } else {
                    continuation.resume(DataSourceResult(result = false)) {}
                }
            }
    }

    suspend fun isSignedIn(): DataSourceResponse<Boolean> {
        val user = Firebase.auth.currentUser
        return if (user == null) {
            DataSourceResult(result = false)
        } else {
            DataSourceResult(result = true)
        }

    }
}
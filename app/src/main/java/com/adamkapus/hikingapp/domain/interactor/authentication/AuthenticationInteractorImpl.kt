package com.adamkapus.hikingapp.domain.interactor.authentication

import com.adamkapus.hikingapp.data.network.AuthenticationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationInteractorImpl/* : AuthenticationInteractor*/ {


    suspend fun isSignedIn(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        val dataSource = AuthenticationDataSource()
        val response = dataSource.isSignedIn()
        response.toInteractorResponse()
    }

    suspend fun getUserInformation(): InteractorResponse<HikingAppUser> =
        withContext(Dispatchers.IO) {
            val dataSource = AuthenticationDataSource()
            val response = dataSource.getUserInfo()
            response.toInteractorResponse()
        }

    suspend fun signUp(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean> =
        withContext(Dispatchers.IO) {
            val dataSource = AuthenticationDataSource()
            val response = dataSource.signUp(emailAddress, password)
            response.toInteractorResponse()
        }

    suspend fun signOut(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        val dataSource = AuthenticationDataSource()
        val response = dataSource.signOut()
        response.toInteractorResponse()
    }

    suspend fun signIn(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        val dataSource = AuthenticationDataSource()
        val response = dataSource.signIn(emailAddress, password)
        response.toInteractorResponse()
    }

}
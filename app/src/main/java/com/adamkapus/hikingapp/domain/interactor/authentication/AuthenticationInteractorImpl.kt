package com.adamkapus.hikingapp.domain.interactor.authentication

import com.adamkapus.hikingapp.data.network.AuthenticationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationInteractorImpl : AuthenticationInteractor {


    override suspend fun isSignedIn(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        //InteractorResult<Boolean>(result = true)
        InteractorError
    }

    override suspend fun getUserInformation(): InteractorResponse<HikingAppUser> =
        withContext(Dispatchers.IO) {
            val dataSource = AuthenticationDataSource()
            val response = dataSource.getUserInfo()
            response.toInteractorResponse()
        }

    override suspend fun signUp(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean> =
        withContext(Dispatchers.IO) {
            val dataSource = AuthenticationDataSource()
            val response = dataSource.signUp(emailAddress, password)
            response.toInteractorResponse()
        }

    override suspend fun signOut(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        val dataSource = AuthenticationDataSource()
        val response = dataSource.signOut()
        response.toInteractorResponse()
    }

    override suspend fun signIn(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        val dataSource = AuthenticationDataSource()
        val response = dataSource.signIn(emailAddress, password)
        response.toInteractorResponse()
    }

}
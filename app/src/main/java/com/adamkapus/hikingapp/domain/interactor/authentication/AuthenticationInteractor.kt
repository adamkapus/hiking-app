package com.adamkapus.hikingapp.domain.interactor.authentication

import com.adamkapus.hikingapp.data.network.AuthenticationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationInteractor @Inject constructor(
    private val authenticationDataSource: AuthenticationDataSource
) {


    suspend fun isSignedIn(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {

        return@withContext authenticationDataSource.isSignedIn().toInteractorResponse()
    }

    suspend fun getUserInformation(): InteractorResponse<HikingAppUser> =
        withContext(Dispatchers.IO) {
            return@withContext authenticationDataSource.getUserInfo().toInteractorResponse()
        }

    suspend fun signUp(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean> =
        withContext(Dispatchers.IO) {

            return@withContext authenticationDataSource.signUp(emailAddress, password).toInteractorResponse()
        }

    suspend fun signOut(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        return@withContext authenticationDataSource.signOut().toInteractorResponse()
    }

    suspend fun signIn(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        return@withContext authenticationDataSource.signIn(emailAddress, password).toInteractorResponse()
    }

}
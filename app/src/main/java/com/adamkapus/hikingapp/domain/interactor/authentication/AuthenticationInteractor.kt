package com.adamkapus.hikingapp.domain.interactor.authentication

import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.authentication.HikingAppUser

interface AuthenticationInteractor {

    suspend fun isSignedIn(): InteractorResponse<Boolean>
    suspend fun getUserInformation(): InteractorResponse<HikingAppUser>

    suspend fun signUp(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean>

    suspend fun signOut(): InteractorResponse<Boolean>

    suspend fun signIn(
        emailAddress: String,
        password: String
    ): InteractorResponse<Boolean>


}
package com.adamkapus.hikingapp.domain.model

import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult

sealed class InteractorResponse<out T>

object InteractorError : InteractorResponse<Nothing>()

data class InteractorResult<T>(val result: T) : InteractorResponse<T>()

fun <T> DataSourceResponse<T>.toInteractorResponse() = when (this) {
    is DataSourceResult -> toInteractorResult()
    is DataSourceError -> toInteractorError()
}

private fun <T> DataSourceResult<T>.toInteractorResult() = InteractorResult(result)
private fun DataSourceError.toInteractorError() = InteractorError
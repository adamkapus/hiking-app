package com.adamkapus.hikingapp.data.model

sealed class DataSourceResponse<out T>

object DataSourceError : DataSourceResponse<Nothing>()

data class DataSourceResult<out T>(val result: T) : DataSourceResponse<T>()
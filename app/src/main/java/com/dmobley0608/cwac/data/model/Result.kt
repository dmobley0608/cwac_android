package com.dmobley0608.cwac.data.model

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception):Result<Nothing>()
}
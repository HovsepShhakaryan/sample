package com.vylo.common.api

sealed class Resource<T>(
    val data: T? = null,
    val errorData: Any? = null,
    val errorMessage: String? = null
) {

    // We'll wrap our data in this 'Success'
    // class in case of success response from api
    class Success<T>(data: T?) : Resource<T>(data = data)

    // We'll pass error message wrapped in this 'Error'
    // class to the UI in case of failure response
    class ApiError<T>(error: Any?) : Resource<T>(errorData = error)
 
    // We'll just pass object of this Loading
    // class, just before making an api call
    class Error<T>(errorMessage: String) : Resource<T>(errorMessage = errorMessage)

}
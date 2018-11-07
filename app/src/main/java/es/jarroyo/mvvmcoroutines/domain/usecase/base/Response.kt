package es.jarroyo.mvvmcoroutines.domain.usecase.base

class Response<T>(
    var data: T? = null,
    var error: String? = null,
    var exception: Exception? = null
)
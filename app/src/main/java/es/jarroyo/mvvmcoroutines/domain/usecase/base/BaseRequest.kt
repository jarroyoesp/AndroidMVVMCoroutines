package es.jarroyo.mvvmcoroutines.domain.usecase.base

interface BaseRequest {
    fun validate(): Boolean
}

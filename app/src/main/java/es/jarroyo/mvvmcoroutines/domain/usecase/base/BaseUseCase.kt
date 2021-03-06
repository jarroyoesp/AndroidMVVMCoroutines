package es.jarroyo.mvvmcoroutines.domain.usecase.base

abstract class BaseUseCase<R : BaseRequest, T>() {

    var request: R? = null

    suspend fun execute(request: R? = null): Response<T> {
        this.request = request

        val validated = request?.validate() ?: true
        if (validated) return run()
        return Response(data = null, error = "Error validating request response")
    }

    abstract suspend fun run(): Response<T>
}

package lt.vitalijus.translator_kmm.core.domain.util

sealed class Source {

    data class Success<T>(val data: T) : Source()
    data class Error(val error: Throwable) : Source()
}

sealed class Resource<T>(
    val data: T?,
    val throwable: Throwable? = null
) {

    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(throwable: Throwable): Resource<T>(data = null, throwable = throwable)
}

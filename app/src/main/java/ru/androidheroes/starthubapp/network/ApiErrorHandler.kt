package ru.androidheroes.starthubapp.network

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import retrofit2.Response
import ru.androidheroes.starthubapp.network.ApiErrorHandler.Companion.STATUS_NO_NETWORK
import java.lang.Exception
import java.net.UnknownHostException

class ApiErrorHandler<T> : ObservableTransformer<Response<T>, T> {

    override fun apply(upstream: Observable<Response<T>>): ObservableSource<T> {
        return upstream
            .onErrorResumeNext { error: Throwable ->
                val throwable = when (error) {
                    is UnknownHostException -> Error.NoNetwork()
                    else -> error
                }
                return@onErrorResumeNext Observable.error(throwable)
            }
            .map { response ->
                return@map if (response.isSuccessful && (response.body() != null)) {
                    response.body()
                } else {
                    throw Exception("Server error")
                }
            }
    }

    companion object {

        const val STATUS_NO_NETWORK = -1
    }
}

sealed class Error(
    val status: Int,
    val code: String,
    val codeValue: String,
    override val message: String?
) : RuntimeException(message) {


    class NoNetwork : Error(STATUS_NO_NETWORK, "", "", "Network unavailable")
}




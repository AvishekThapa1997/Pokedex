package com.example.pokedex.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: suspend () -> ResultType,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
    crossinline onFetchFailed: (Throwable) -> Unit = { }
) = channelFlow {
    val data = query()

    if (shouldFetch(data)) {
        send(Resource.Loading(data))
        try {
            saveFetchResult(fetch())
            send(Resource.Success(query()))
        } catch (t: Throwable) {
            onFetchFailed(t)
            send(Resource.Error(t, null))
        }
    } else {
        send(Resource.Success(data))
    }
}.flowOn(Dispatchers.IO)
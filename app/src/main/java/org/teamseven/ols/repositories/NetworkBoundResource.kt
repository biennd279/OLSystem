package org.teamseven.ols.repositories

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.teamseven.ols.utils.AppExecutors
import org.teamseven.ols.utils.Resource
import retrofit2.Response

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow() = flow<Resource<ResultType>> {

        emit(Resource.loading(data = null))
        val dbSource = query()
            .catch { e ->
                emit(Resource.error(null, e.message.toString()))
            }
            .firstOrNull()

        if (shouldFetch(dbSource)) {
            emit(Resource.loading(dbSource))
            val response = fetch()
            if (isSuccessful(response)) {
                if (hasBody(response)) {
                    try {
                        val value = processResponse(response)
                        saveCallResult(value)
                        emit(Resource.success(value))
                    } catch (cause: Throwable) {
                        onFetchFailed()
                        emit(Resource.error(dbSource, "Error when save: ${cause.message}"))
                    }
                } else {
                    emit(Resource.success(dbSource!!))
                }
            } else {
                emit(Resource.error(null, "Can not refresh: ${response.errorBody()}"))
            }
        } else {
            emit(Resource.success(dbSource!!))
        }
    }.flowOn(Dispatchers.IO)

    protected open fun onFetchFailed() {}

    @MainThread
    protected abstract fun query(): Flow<ResultType>

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract suspend fun fetch(): Response<RequestType>

    @WorkerThread
    protected abstract fun processResponse(response: Response<RequestType>): ResultType

    @WorkerThread
    protected abstract suspend fun saveCallResult(item: ResultType)

    @MainThread
    protected open fun isSuccessful(response: Response<RequestType>) = response.isSuccessful

    @MainThread
    protected open fun hasBody(response: Response<RequestType>) = response.body() != null
}
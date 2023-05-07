package com.example.mystoryapp.data.repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.data.database.StoryDatabase
import com.example.mystoryapp.data.model.ListStory
import com.example.mystoryapp.data.model.LoginResponse
import com.example.mystoryapp.data.model.StoryResponse
import com.example.mystoryapp.data.network.ApiService
import com.example.mystoryapp.data.network.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService, private val storyDatabase: StoryDatabase) {
    suspend fun login(email: String, password: String): LiveData<Result<LoginResponse?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response.loginResult))
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "On Failure : ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun register(name: String, email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        val registerResponse = MutableLiveData<StoryResponse>()
        try {
            val response = apiService.register(name, email, password)
            registerResponse.postValue(response)
            emit(Result.Success(response.message))
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "On Failure : ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?,
    ): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(token, image, description, lat, lon)
            emit(Result.Success(response.message))
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "On Failure : ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllStories(token: String): LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            },
        ).liveData
    }

    fun getMaps(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getMaps(token, 1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "On Failure : ${e.message}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, storyDatabase: StoryDatabase): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDatabase).also {
                    instance = it
                }
            }
    }
}

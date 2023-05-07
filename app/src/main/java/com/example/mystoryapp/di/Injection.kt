package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.database.StoryDatabase
import com.example.mystoryapp.data.network.ApiConfig
import com.example.mystoryapp.data.repository.StoryRepository

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getInstance(context)
        return StoryRepository.getInstance(apiService, database)
    }
}

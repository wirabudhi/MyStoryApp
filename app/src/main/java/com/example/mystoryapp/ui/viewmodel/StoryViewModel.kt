package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    suspend fun uploadStory(
        token: String,
        image: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?,
    ) = storyRepository.uploadStory(token, image, description, lat, lon)
}

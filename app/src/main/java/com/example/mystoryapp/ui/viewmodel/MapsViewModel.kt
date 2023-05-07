package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.StoryRepository

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getMaps(token: String) = storyRepository.getMaps(token)
}
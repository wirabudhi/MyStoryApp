package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    suspend fun register(name: String, email: String, password: String) = storyRepository.register(name, email, password)
}

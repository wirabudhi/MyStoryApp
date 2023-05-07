package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mystoryapp.data.repository.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    suspend fun login(username: String, password: String) = storyRepository.login(username, password)
}

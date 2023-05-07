package com.example.mystoryapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.model.ListStory
import com.example.mystoryapp.data.repository.StoryRepository

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(token: String): LiveData<PagingData<ListStory>> = storyRepository.getAllStories(token).cachedIn(viewModelScope)
}

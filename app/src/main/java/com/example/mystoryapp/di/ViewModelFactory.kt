package com.example.mystoryapp.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.example.mystoryapp.ui.viewmodel.MapsViewModel
import com.example.mystoryapp.ui.viewmodel.RegisterViewModel
import com.example.mystoryapp.ui.viewmodel.StoryViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(storyRepository) as T
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> StoryViewModel(storyRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(storyRepository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(storyRepository) as T
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> MapsViewModel(storyRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: ViewModelFactory(Injection.provideStoryRepository(context)).also { INSTANCE = it }
    }
}

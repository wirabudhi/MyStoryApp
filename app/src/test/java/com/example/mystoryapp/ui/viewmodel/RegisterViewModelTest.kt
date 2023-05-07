package com.example.mystoryapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.DataDummy
import com.example.mystoryapp.data.network.Result
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyName = DataDummy.generateDummyName()
    private val dummyEmail = DataDummy.generateDummyEmail()
    private val dummyPassword = DataDummy.generateDummyPassword()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when register Should Not Null and Return Success`() = runBlockingTest {
        val expectedRegister = MutableLiveData<Result<String>>()
        expectedRegister.value = Result.Success("User Created")
        Mockito.`when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)
        val actualRegister = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
        assertNotNull(actualRegister)
        assertTrue(actualRegister is Result.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when register Error Should Return Error`() = runBlockingTest {
        val expectedRegister = MutableLiveData<Result<String>>()
        expectedRegister.value = Result.Error("Error")
        Mockito.`when`(storyRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(expectedRegister)
        val actualRegister = registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).register(dummyName, dummyEmail, dummyPassword)
        assertNotNull(actualRegister)
        assertTrue(actualRegister is Result.Error)
    }
}

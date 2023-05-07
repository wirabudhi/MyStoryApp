package com.example.mystoryapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.DataDummy
import com.example.mystoryapp.data.model.LoginResponse
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
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLogin = DataDummy.generateDummyLoginResult()
    private val dummyEmail = DataDummy.generateDummyEmail()
    private val dummyPassword = DataDummy.generateDummyPassword()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when login Should Not Null and Return Success`() = runBlockingTest {
        val expectedLogin = MutableLiveData<Result<LoginResponse?>>()
        expectedLogin.value = Result.Success(dummyLogin)
        `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)
        val actualLogin = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        assertNotNull(actualLogin)
        assertTrue(actualLogin is Result.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when login Error Should Return Error`() = runBlockingTest {
        val expectedLogin = MutableLiveData<Result<LoginResponse?>>()
        expectedLogin.value = Result.Error("Error")
        `when`(storyRepository.login(dummyEmail, dummyPassword)).thenReturn(expectedLogin)
        val actualLogin = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyRepository).login(dummyEmail, dummyPassword)
        assertNotNull(actualLogin)
        assertTrue(actualLogin is Result.Error)
    }
}

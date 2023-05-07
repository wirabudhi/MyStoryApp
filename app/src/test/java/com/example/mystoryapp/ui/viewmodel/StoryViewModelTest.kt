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
class StoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var addViewModel: StoryViewModel
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyPhoto = DataDummy.generateDummyImages()
    private val dummyDesc = DataDummy.generateDummyDescription()
    private val dummyLat = DataDummy.generateDummyLat()
    private val dummyLon = DataDummy.generateDummyLon()

    @Before
    fun setUp() {
        addViewModel = StoryViewModel(storyRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when uploadStory Should Not Null and Return Success`() = runBlockingTest {
        val expectedUploadStory = MutableLiveData<Result<String>>()
        expectedUploadStory.value = Result.Success("Upload Success")
        Mockito.`when`(storyRepository.uploadStory(dummyToken, dummyPhoto, dummyDesc, dummyLat, dummyLon)).thenReturn(expectedUploadStory)
        val actualUploadStory = addViewModel.uploadStory(dummyToken, dummyPhoto, dummyDesc, dummyLat, dummyLon).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(dummyToken, dummyPhoto, dummyDesc, dummyLat, dummyLon)
        assertNotNull(actualUploadStory)
        assertTrue(actualUploadStory is Result.Success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when uploadStory Error and Return Error`() = runBlockingTest {
        val expectedUploadStory = MutableLiveData<Result<String>>()
        expectedUploadStory.value = Result.Error("Error")
        Mockito.`when`(storyRepository.uploadStory(dummyToken, dummyPhoto, dummyDesc, dummyLat, dummyLon)).thenReturn(expectedUploadStory)
        val actualUploadStory = addViewModel.uploadStory(dummyToken, dummyPhoto, dummyDesc, dummyLat, dummyLon).getOrAwaitValue()

        Mockito.verify(storyRepository).uploadStory(dummyToken, dummyPhoto, dummyDesc, dummyLat, dummyLon)
        assertNotNull(actualUploadStory)
        assertTrue(actualUploadStory is Result.Error)
    }
}

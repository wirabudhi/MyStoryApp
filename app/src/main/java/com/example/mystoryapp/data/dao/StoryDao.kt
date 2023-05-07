package com.example.mystoryapp.data.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapp.data.model.ListStory

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun uploadStory(listStory: List<ListStory>)

    @Query("SELECT * FROM ListStory")
    fun getAllStories(): PagingSource<Int, ListStory>

    @Query("DELETE FROM ListStory")
    suspend fun deleteAllStories()
}

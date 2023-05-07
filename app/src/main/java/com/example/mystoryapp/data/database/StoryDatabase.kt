package com.example.mystoryapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mystoryapp.data.dao.RemoteKeysDao
import com.example.mystoryapp.data.dao.StoryDao
import com.example.mystoryapp.data.model.ListStory
import com.example.mystoryapp.data.model.RemoteKeys

@Database(entities = [ListStory::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                StoryDatabase::class.java,
                "story.db",
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}

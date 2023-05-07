package com.example.mystoryapp.data.model

import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginResponse?,

    @field:SerializedName("listStory")
    val listStory: List<ListStory>?
)

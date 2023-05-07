package com.example.mystoryapp

import com.example.mystoryapp.data.model.ListStory
import com.example.mystoryapp.data.model.LoginResponse
import com.example.mystoryapp.data.model.StoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyLoginResult(): LoginResponse {
        return LoginResponse(
            "113344",
            "Test 1134",
            "123456",
            true,
        )
    }

    fun generateDummyName(): String {
        return "Test 1134"
    }

    fun generateDummyEmail(): String {
        return "test1134@gmail.com"
    }

    fun generateDummyPassword(): String {
        return "Password"
    }

    fun generateDummyToken(): String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTlPQ1p2em83SWg4dnlPR0EiLCJpYXQiOjE2ODMyOTAwMTZ9.cgGUiAUDnzhG3FrzaTAY7IbN_GFGODC8AxW9btHBwtk"
    }

    fun listStoryItem(): List<ListStory> {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..100) {
            val list = ListStory(
                "$i",
                "name: $i",
                "description: $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/homepage-hero.png",
                -8.54953,
                115.1246933,
            )
            items.add(list)
        }
        return items
    }

    fun generateDummyImages(): MultipartBody.Part {
        return MultipartBody.Part.createFormData("twitter", "https://story-api.dicoding.dev/images/stories/photos-1665640858424_MIPUfQux.png")
    }

    fun generateDummyDescription(): RequestBody {
        return "desc".toRequestBody("text/plain".toMediaType())
    }

    fun generateDummyLat(): Double {
        return -8.54953
    }

    fun generateDummyLon(): Double {
        return 115.1246933
    }
}

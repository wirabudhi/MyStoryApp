package com.example.mystoryapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,
) : Parcelable

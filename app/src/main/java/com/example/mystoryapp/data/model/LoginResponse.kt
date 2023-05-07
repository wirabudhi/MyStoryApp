package com.example.mystoryapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("token")
    var token: String? = null,

    @field:SerializedName("isLogin")
    var isLogin: Boolean = false,
) : Parcelable

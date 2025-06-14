package com.example.phonecheckapp1.data.remote.api.phoneNum

import com.example.phonecheckapp1.data.remote.dto.PhoneInfo
import retrofit2.http.GET
import retrofit2.http.Query

interface PhoneApi {
    @GET("v2/verify")
    suspend fun getPhoneInfo(
        @Query("phone") phoneNum: String,
        @Query("key") key: String = "Your api key here"
    ): PhoneInfo
}
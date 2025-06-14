package com.example.phonecheckapp1.di.module

import com.example.phonecheckapp1.data.remote.api.phoneNum.PhoneApi
import com.example.phonecheckapp1.ui.phone_num.PhoneNumViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.veriphone.io/"

val phoneNumModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
    }

    single<PhoneApi>{
        get<Retrofit>().create(PhoneApi::class.java)
    }

    viewModel {
        PhoneNumViewModel(get())
    }
}
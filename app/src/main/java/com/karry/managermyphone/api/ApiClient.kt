package com.karry.managermyphone.api

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class ApiClient {
    companion object {
        val client: Retrofit
            get() = Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/fcm/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
    }
}
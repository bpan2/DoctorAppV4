package com.example.doctorapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PatientService {
        var baseURL = "http://10.0.2.2:3000"

        private val retrofitBuilder = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build()

        // The Api instance
        val retrofitClient = retrofitBuilder.create(PatientApi::class.java)
}
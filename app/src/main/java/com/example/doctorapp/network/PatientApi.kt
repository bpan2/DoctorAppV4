package com.example.doctorapp.network

import com.example.doctorapp.models.PatientEntity
import retrofit2.http.*

interface PatientApi {
    @GET("/patient/fetch/")
    suspend fun getAllPatients(): List<PatientEntity>

    @GET("/patient/{id}")
    suspend fun getPatient(@Query("id") id: String): PatientEntity

    @POST("/patient")
    suspend fun addPatient(@Body params: PatientEntity): Int

    @PATCH("/patient/{id}")
    suspend fun updatePatient(@Path("id") id: Int, @Body params: PatientEntity): Int

    @DELETE("/patient/{id}")
    suspend fun deletePatient(@Path("id") id: Int): Int
}
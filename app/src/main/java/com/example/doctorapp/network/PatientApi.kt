package com.example.doctorapp.network

import com.example.doctorapp.models.PatientEntity
import retrofit2.http.*

interface PatientApi {
    @GET("/patient/fetch/")
    suspend fun getAllPatients(): List<PatientEntity>

    @GET("/patient/fetchbyid/{id}")
    suspend fun getPatient(@Query("id") id: Int): PatientEntity

    @POST("/patient/add")
    suspend fun addPatient(@Query("_id") id: Int, @Query("patient_name")patient_name: String,  @Query("patient_DOB") patient_DOB: String?, @Query("patient_gender") patient_gender: String?)

    @PATCH("/patient/edit/{id}")
    suspend fun updatePatient(@Path("id") id: Int, @Body params: PatientEntity)

    @DELETE("/patient/delete/{id}")
    suspend fun deletePatient(@Path("id") id: Int)
}
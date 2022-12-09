package com.example.doctorapp.network

import com.example.doctorapp.models.PatientEntity
import com.example.doctorapp.models.PatientMedicalRecord
import org.json.JSONObject
import retrofit2.http.*

interface PatientApi {
    @GET("/patient/fetch/")
    suspend fun getAllPatients(): List<PatientEntity>

    @GET("/patient/fetchbyid/{id}")
    suspend fun getPatient(@Query("id") id: Int): PatientEntity

    @POST("/patient/add")
    suspend fun addPatient(@Query("_id") id: Int, @Query("patient_name")patient_name: String?,  @Query("patient_OHIP")patient_OHIP: String?, @Query("patient_DOB") patient_DOB: String?, @Query("patient_gender") patient_gender: String?, @Query("patient_phone") patient_phone: String?, @Query("patient_email")patient_email: String?, @Body medicalrecord: JSONObject?)

    @PATCH("/patient/edit/{id}")
    suspend fun updatePatient(@Path("id") id: Int, @Body params: PatientEntity)

    @DELETE("/patient/delete/{id}")
    suspend fun deletePatient(@Path("id") id: Int)
}

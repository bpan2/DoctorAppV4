package com.example.doctorapp.models

import com.example.doctorapp.NEW_PATIENT_ID

data class Patient(
    val id: Int,
    val patient_name: String,
    val patient_OHIP: String,
    val patient_DOB: String,
    val patient_gender: String,
    val patient_phone: String,
    val patient_address: String,
    val patient_email: String,
    val patient_medicalrecord: PatientMedicalRecord,
)

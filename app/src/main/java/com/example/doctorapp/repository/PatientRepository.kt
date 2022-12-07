package com.example.doctorapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.doctorapp.database.AppDatabase
import com.example.doctorapp.models.PatientEntity
import com.example.doctorapp.network.PatientService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import kotlinx.coroutines.launch

class PatientRepository(application: Application) {
    var allPatients: LiveData<List<PatientEntity>>?
    var searchResults = MutableLiveData<List<PatientEntity>>()
    var db: AppDatabase? = null


    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        db = AppDatabase.getInstance(application)
        allPatients = db?.patientDao()?.getAllPatients()

        //if the local dbs is empty, retrieve data from the remote sources
        if (allPatients?.value == null) {
            coroutineScope.launch {
                val patientList = PatientService.retrofitClient.getAllPatients()
                withContext(Dispatchers.IO) {
                    db?.patientDao()?.insertAll(patientList)
                }
            }
        }
    }


    //operations on a single patient
    suspend fun insertPatient(patientEntity: PatientEntity) {
        coroutineScope.launch {
            PatientService.retrofitClient.addPatient(patientEntity.id, patientEntity.patient_name, patientEntity.patient_DOB, patientEntity.patient_gender)
        }
        db?.patientDao()?.insertPatient(patientEntity)
    }


    suspend fun getPatientById(id: Int) = db?.patientDao()?.getPatientById(id)
    suspend fun updatePatient(patientEntity: PatientEntity) =
        db?.patientDao()?.updatePatient(patientEntity)

    suspend fun deletePatientById(id: Int) = db?.patientDao()?.deletePatientById(id)
    suspend fun deletePatient(patient: PatientEntity) = db?.patientDao()?.deletePatient(patient)


    //operations on multiple or all patients
    suspend fun insertAll(patients: List<PatientEntity>) = db?.patientDao()?.insertAll(patients)
    suspend fun getAllPatients(): LiveData<List<PatientEntity>>? =
        db?.patientDao()?.getAllPatients()

    suspend fun deleteAllPatients() = db?.patientDao()?.deleteAllPatients()
    suspend fun deletePatients(selectedPatients: List<PatientEntity>) {
        //delete the selected patients on the microservice
        selectedPatients.forEach{
            coroutineScope.launch {
                PatientService.retrofitClient.deletePatient(it.id)
            }
        }

        //delete the selected patients on Room dbs
        db?.patientDao()?.deletePatients(selectedPatients)
    }

}
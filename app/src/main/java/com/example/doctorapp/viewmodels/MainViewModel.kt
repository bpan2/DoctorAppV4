package com.example.doctorapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.doctorapp.database.AppDatabase
import com.example.doctorapp.models.PatientEntity
import com.example.doctorapp.models.SampleDataProvider
import com.example.doctorapp.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PatientRepository = PatientRepository(application)
    val patientsList: LiveData<List<PatientEntity>>? = repository.allPatients
    private val searchResults: MutableLiveData<List<PatientEntity>> = repository.searchResults

    fun getSearchResults(): MutableLiveData<List<PatientEntity>> {
        return searchResults
    }


    //operations on individual patient
    suspend fun insertPatient(patient: PatientEntity) {
        repository.insertPatient(patient)
    }
    suspend fun getPatientById(id: Int) {
        repository.getPatientById(id)
    }
    suspend fun updatePatient(patient: PatientEntity){
        repository.updatePatient(patient)
    }
    suspend fun deletePatient(patient: PatientEntity) {
        repository.deletePatient(patient)
    }
    suspend fun deletePatientById(id: Int){
        repository.deletePatientById(id)
    }


    //operations on all patients
    fun getAllPatients(): LiveData<List<PatientEntity>>? {
        return patientsList
    }
    suspend fun insertAll(patients: List<PatientEntity>){
        repository.insertAll(patients)
    }
    suspend fun deleteAllPatients(){
        repository.deleteAllPatients()
    }

    fun addSampleData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val samplePatients = SampleDataProvider.getPatients()
                repository.insertAll(samplePatients)
            }
        }
    }

    fun deletePatients(selectedPatients: List<PatientEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.deletePatients(selectedPatients)
            }
        }
    }


}

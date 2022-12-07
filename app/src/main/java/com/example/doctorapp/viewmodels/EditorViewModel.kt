package com.example.doctorapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorapp.NEW_PATIENT_ID
import com.example.doctorapp.models.PatientEntity
import com.example.doctorapp.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PatientRepository = PatientRepository(application)
    val currentPatient = MutableLiveData<PatientEntity?>()

    fun getPatientById(patientId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val patient =
                    if(patientId != NEW_PATIENT_ID){
                        repository.getPatientById(patientId)
                    }else{
                        PatientEntity()
                    }
                currentPatient.postValue(patient)
            }
        }
    }

    fun updatePatient() {
        currentPatient.value?.let{
            it.patient_name = it.patient_name.trim()

            //if the id is the default constant "NEW_PATIENT_ID" and the name field of the patient's record is empty, don't update it
            if(it.id == NEW_PATIENT_ID && it.patient_name.isEmpty()){
                return
            }

            viewModelScope.launch {
                withContext(Dispatchers.IO){

                    // !!!!! if the name field of the patient is empty,
                    // !!!!! the entire record of the current patient will be deleted from the database
                    if(it.patient_name.isEmpty()){
                        repository.deletePatient(it)
                    }else{
                        repository.insertPatient(it)
                    }
                }
            }
        }
    }
}
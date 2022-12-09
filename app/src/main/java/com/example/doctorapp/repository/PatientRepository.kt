package com.example.doctorapp.repository

import android.R
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.doctorapp.database.AppDatabase
import com.example.doctorapp.models.PatientEntity
import com.example.doctorapp.models.PatientMedicalRecord
import com.example.doctorapp.network.PatientService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import com.google.gson.JsonArray as GsonJsonArray


class PatientRepository(application: Application) {
    var allPatients: LiveData<List<PatientEntity>>?
    var searchResults = MutableLiveData<List<PatientEntity>>()
    var db: AppDatabase? = null


    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        db = AppDatabase.getInstance(application)
        var isEmpty: Boolean
        //var isOnline: Boolean

        coroutineScope.launch {
            withContext(Dispatchers.IO){
                isEmpty = db?.patientDao()?.isEmpty() == true
                //isOnline = isNetworkAvailable(application)
            }

            //if the Internet access is available and the local dbs is empty, retrieve data from the remote sources
            if (isOnline(application) == true)  {
                if(isEmpty){
                    val patientList = PatientService.retrofitClient.getAllPatients()
                    withContext(Dispatchers.IO) {
                        db?.patientDao()?.insertAll(patientList)
                    }
                }
            }else{
                AlertDialog.Builder(application)
                    .setTitle("Connection Failure")
                    .setMessage("Please Connect to the Internet")
                    .setPositiveButton(R.string.ok,
                        DialogInterface.OnClickListener { dialog, which -> })
                    .setIcon(R.drawable.ic_dialog_alert)
                    .show()
            }

        }

        allPatients = db?.patientDao()?.getAllPatients()
    }


    //operations on a single patient
    suspend fun insertPatient(patientEntity: PatientEntity) {
        coroutineScope.launch {
            //val medicalrecord = (GsonJsonArray) new Gson().toJsonTree(patientEntity.patient_medicalrecord)
            val medicalrecord: JSONObject? = null

            PatientService.retrofitClient.addPatient(patientEntity.id, patientEntity.patient_name, patientEntity.patient_OHIP, patientEntity.patient_DOB, patientEntity.patient_gender, patientEntity.patient_phone, patientEntity.patient_email, medicalrecord)
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
       /* selectedPatients.forEach{
            coroutineScope.launch {
                PatientService.retrofitClient.deletePatient(it.id)
            }
        }*/

        //delete the selected patients on Room dbs
        db?.patientDao()?.deletePatients(selectedPatients)
    }


    //The following function is retrieved from
    //https://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->    true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->   true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->   true
                else ->     false
            }
        }
        // For below 29 api
        else {
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }


    //https://stackoverflow.com/questions/51141970/check-internet-connectivity-android-in-kotlin
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun convertPatientMedicalRecordToJSONObject(height_cm: Int?,
                                                weight_kg: Int?,
                                                blood_pressure_kPa: Int?,
                                                allergies: String?,
                                                asthma: Boolean?,
                                                diabetes: Boolean?,
                                                hospitalization: String?,
                                                seizures: Boolean?,
                                                chest_pain: Boolean?,
                                                headaches: Boolean?,

                                                heart_attack: Boolean?,
                                                concussion: Boolean?,
                                                muscle_cramps: Boolean?,
                                                orthotics: Boolean?) : JSONObject? {
        var medicalrecord: JSONObject? = null

           // PatientMedicalRecord(height_cm, weight_kg, blood_pressure_kPa, allergies, asthma, diabetes, hospitalization, seizures, chest_pain, headaches, heart_attack, concussion, muscle_cramps, orthotics)

        return medicalrecord
    }

}
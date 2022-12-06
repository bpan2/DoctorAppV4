package com.example.doctorapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.doctorapp.models.PatientEntity

@Database(entities = [PatientEntity::class], version = 1, exportSchema = false)
//@TypeConverters(DataConverter::class)

abstract class AppDatabase: RoomDatabase() {

    abstract fun patientDao(): PatientDao?

    companion object{
        private var  INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
package com.example.doctorapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.doctorapp.NEW_PATIENT_ID
import java.util.*


@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "patient_name") var patient_name: String,
    @ColumnInfo(name = "patient_OHIP") var patient_OHIP: String?,
    @ColumnInfo(name = "patient_DOB") var patient_DOB: String?,
    @ColumnInfo(name = "patient_gender") var patient_gender: String?,
    @ColumnInfo(name = "patient_phone") var patient_phone: String?,
    @ColumnInfo(name = "patient_email") var patient_email: String?,
    @ColumnInfo(name = "patient_address") var patient_address: String?,
    @Embedded var patient_medicalrecord: PatientMedicalRecord?,
) {
    constructor() : this(NEW_PATIENT_ID,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        null
    )

    constructor(
        name: String,
        OHIP: String,
        DOB: String,
        gender: String,
        phone: String,
        email: String,
        address: String,
    ) : this(NEW_PATIENT_ID, name, OHIP, DOB, gender, phone, email, address, null)
}
/*{
  *//* constructor() : this(
        "John",
        "zz9876543210",
        "2000-01-01",
        "male",
        "416-777-88888",
        "john@gmail.com",
        "12345 Yonge Street, Toronto, ON"
    )*//*


    constructor(
        name: String,
        OHIP: String,
        DOB: String,
        gender: String,
        phone: String,
        email: String,
        address: String,
        patient_medicalrecord: PatientMedicalRecord
    ) : this() {
        this.patient_name = name
        this.patient_OHIP = OHIP
        this.patient_DOB = DOB
        this.patient_gender = gender
        this.patient_phone = phone
        this.patient_email = email
        this.patient_address = address
        this.patient_medicalrecord = patient_medicalrecord
    }
}*/

@kotlinx.serialization.Serializable
data class PatientMedicalRecord(
    var height_cm: Int?,
    var weight_kg: Int?,
    var blood_pressure_kPa: Int?,
    var allergies: String?,
    var asthma: Boolean?,
    var diabetes: Boolean?,
    var hospitalization: String?,
    var seizures: Boolean?,
    var chest_pain: Boolean?,
    var headaches: Boolean?,
    var heart_attack: Boolean?,
    var concussion: Boolean?,
    var muscle_cramps: Boolean?,
    var orthotics: Boolean?
    //val medical_reports: MedicalReport?
)


//https://developer.android.com/training/data-storage/room/relationships#nested-objects
/**
 * Create embedded objects

Sometimes, you'd like to express an entity or data object as a cohesive whole in your database logic,
even if the object contains several fields.

In these situations, you can use the @Embedded annotation to represent an object
that you'd like to decompose into its subfields within a table.
You can then query the embedded fields just as you would for other individual columns.

For instance, your User class can include a field of type Address,
which represents a composition of fields named street, city, state, and postCode.

To store the composed columns separately in the table,
include an Address field in the User class that is annotated with @Embedded,
as shown in the following code snippet:

Kotlin:

data class Address(
val street: String?,
val state: String?,
val city: String?,
@ColumnInfo(name = "post_code") val postCode: Int
)


@Entity
data class User(
@PrimaryKey val id: Int,
val firstName: String?,
@Embedded val address: Address?
)

The table representing a User object then contains columns
with the following names: id, firstName, street, state, city, and post_code.

 */
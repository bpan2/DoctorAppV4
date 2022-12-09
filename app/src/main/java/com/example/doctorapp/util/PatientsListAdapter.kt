package com.example.doctorapp.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorapp.R
import com.example.doctorapp.databinding.ListItemBinding
import com.example.doctorapp.models.PatientEntity

class PatientsListAdapter (private val patientsList: List<PatientEntity>, private val listener: ListItemListener
                           ): RecyclerView.Adapter<PatientsListAdapter.ViewHolder>(){

    val selectedPatients = arrayListOf<PatientEntity>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = patientsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val patient = patientsList[position]
        with(holder.binding){
            nameChip.text = "Name: " + patient.patient_name
            OHIPChip.text = "OHIP: " + patient.patient_OHIP
            DOBChip.text = "Date-of-birth:" + patient.patient_DOB
            root.setOnClickListener{
                listener.onItemClick(patient.id)
            }

            //The following two sections handles the leading icon changes when an item of patient is selected or unselected in the list
            //Section 1: Show the leading icon with a check mark for each item if the user selects it.
            //Ff the user unchecks the item, the leading icon will go back to be a person icon prior to the selection.
            fab.setOnClickListener {
                if(selectedPatients.contains(patient)){
                    selectedPatients.remove(patient)
                    fab.setImageResource(R.drawable.ic_person)
                }else{
                    selectedPatients.add(patient)
                    fab.setImageResource(R.drawable.ic_check)
                }
                listener.onItemSelectionChanged()
            }
            //Section 2: To maintain the selection state during the runtime when the user scrolls the Recyclerview
            fab.setImageResource(
                if(selectedPatients.contains(patient)){
                    R.drawable.ic_check
                }else{
                    R.drawable.ic_person
                }
            )

        }
    }

    interface ListItemListener{
        fun onItemClick(patientId: Int)
        fun onItemSelectionChanged()
    }

}
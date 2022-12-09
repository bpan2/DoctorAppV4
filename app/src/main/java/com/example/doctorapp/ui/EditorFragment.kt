package com.example.doctorapp.ui

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.doctorapp.NEW_PATIENT_ID
import com.example.doctorapp.R
import com.example.doctorapp.databinding.FragmentEditorBinding
import com.example.doctorapp.viewmodels.EditorViewModel

class EditorFragment : Fragment() {

    private lateinit var viewModel: EditorViewModel
    private val args: EditorFragmentArgs by navArgs()
    private lateinit var binding: FragmentEditorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //create the up-button(by using check icon) on the action bar to navigate back to the home screen
        (activity as AppCompatActivity).supportActionBar?.let{
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check_circle)
        }
        setHasOptionsMenu(true)

        //set the title of the fragment to reflect the actions of adding a new patient and editing an existing patient
        requireActivity().title =
            if(args.patientId == NEW_PATIENT_ID){
                "Add Patient"
            }else{
                "Edit Patient"
            }

        viewModel = ViewModelProvider(this).get(EditorViewModel::class.java)
        binding = FragmentEditorBinding.inflate(inflater, container, false)

        //set all the input fields blank when starting the action of adding a new patient
        binding.nameOutlinedTextField.editText?.setText("")
        binding.OHIPIdOutlinedTextField.editText?.setText("")
        binding.dobOutlinedTextField.editText?.setText("")
        binding.genderOutlinedTextField.editText?.setText("")
        binding.phoneOutlinedTextField.editText?.setText("")
        binding.addressOutlinedTextField.editText?.setText("")
        binding.emailOutlinedTextField.editText?.setText("")
        binding.heightCm.editText?.setText("")
        binding.weightKg.editText?.setText("")
        binding.bloodPressureKPa.editText?.setText("")
        binding.allergies.editText?.setText("")
        binding.asthma.editText?.setText("")
        binding.diabetes.editText?.setText("")
        binding.hospitalization.editText?.setText("")
        binding.seizures.editText?.setText("")
        binding.chestPain.editText?.setText("")
        binding.headaches.editText?.setText("")
        binding.heartAttack.editText?.setText("")
        binding.concussion.editText?.setText("")
        binding.muscleCramps.editText?.setText("")
        binding.orthotics.editText?.setText("")



        //enable the device's back-button or back gesture to navigate from Editor screen back to Home Screen
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    saveAndReturn()
                }
            }
        )

        viewModel.currentPatient.observe(viewLifecycleOwner, Observer {
            binding.nameOutlinedTextField.editText?.setText(it?.patient_name ?: "")
            binding.OHIPIdOutlinedTextField.editText?.setText(it?.patient_OHIP ?: "")
            binding.dobOutlinedTextField.editText?.setText(it?.patient_DOB ?: "")
            binding.genderOutlinedTextField.editText?.setText(it?.patient_gender ?:"")
            binding.phoneOutlinedTextField.editText?.setText(it?.patient_phone ?:"")
            binding.addressOutlinedTextField.editText?.setText(it?.patient_address ?:"")
            binding.emailOutlinedTextField.editText?.setText(it?.patient_email ?:"")
            binding.heightCm.editText?.setText(it?.patient_medicalrecord?.height_cm.toString())
            binding.weightKg.editText?.setText(it?.patient_medicalrecord?.weight_kg.toString())
            binding.bloodPressureKPa.editText?.setText(it?.patient_medicalrecord?.blood_pressure_kPa.toString())
            binding.allergies.editText?.setText( it?.patient_medicalrecord?.allergies ?: "unknown")
            binding.asthma.editText?.setText( it?.patient_medicalrecord?.asthma.toString())
            binding.diabetes.editText?.setText(it?.patient_medicalrecord?.diabetes.toString())
            binding.hospitalization.editText?.setText(it?.patient_medicalrecord?.hospitalization ?: "unknown")
            binding.seizures.editText?.setText(it?.patient_medicalrecord?.seizures.toString())
            binding.chestPain.editText?.setText(it?.patient_medicalrecord?.chest_pain.toString())
            binding.headaches.editText?.setText(it?.patient_medicalrecord?.headaches.toString())
            binding.heartAttack.editText?.setText(it?.patient_medicalrecord?.heart_attack.toString())
            binding.concussion.editText?.setText(it?.patient_medicalrecord?.concussion.toString())
            binding.muscleCramps.editText?.setText(it?.patient_medicalrecord?.muscle_cramps.toString())
            binding.orthotics.editText?.setText(it?.patient_medicalrecord?.orthotics.toString())
        })

        viewModel.getPatientById(args.patientId)

        return binding.root
    }

    //enable the funcationality of the up-button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> saveAndReturn()
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun saveAndReturn(): Boolean {
        //disable the sof keyboard
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)


        viewModel.currentPatient.value?.patient_name = binding.nameOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_OHIP = binding.OHIPIdOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_DOB = binding.dobOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_gender = binding.genderOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_phone = binding.phoneOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_email = binding.emailOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_address = binding.addressOutlinedTextField.editText?.text.toString()
        viewModel.currentPatient.value?.patient_medicalrecord?.height_cm = binding.heightCm.editText?.text?.trim().toString().toInt()
        viewModel.currentPatient.value?.patient_medicalrecord?.weight_kg = binding.weightKg.editText?.text?.trim().toString().toInt()
        viewModel.currentPatient.value?.patient_medicalrecord?.blood_pressure_kPa = binding.bloodPressureKPa.editText?.text?.trim().toString().toInt()
        viewModel.currentPatient.value?.patient_medicalrecord?.allergies = binding.allergies?.editText?.text.toString()
        viewModel.currentPatient.value?.patient_medicalrecord?.asthma = binding.asthma?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.diabetes = binding.diabetes?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.hospitalization = binding.hospitalization?.editText?.text.toString()
        viewModel.currentPatient.value?.patient_medicalrecord?.seizures = binding.seizures?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.chest_pain = binding.chestPain?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.headaches = binding.headaches?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.heart_attack = binding.heartAttack?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.concussion = binding.concussion?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.muscle_cramps = binding.muscleCramps?.editText?.text?.trim().toString().toBoolean()
        viewModel.currentPatient.value?.patient_medicalrecord?.orthotics = binding.orthotics?.editText?.text?.trim().toString().toBoolean()


        viewModel.updatePatient()

        findNavController().navigateUp()
        return true
    }

    //saving data during the orientation change is not implemented
    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }*/
}
package com.example.doctorapp.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doctorapp.NEW_PATIENT_ID
import com.example.doctorapp.R
import com.example.doctorapp.util.PatientsListAdapter
import com.example.doctorapp.TAG
import com.example.doctorapp.databinding.FragmentMainBinding
import com.example.doctorapp.viewmodels.MainViewModel

class MainFragment : Fragment(), PatientsListAdapter.ListItemListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: PatientsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //disable the display of up-button on Home Screen's Action Bar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //enable Options Menu in fragment
        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.app_name)

        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        with(binding.recyclerView){
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.patientsList?.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, it.toString())
            adapter = PatientsListAdapter(it, this@MainFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        })

        binding.floatingActionButton.setOnClickListener {
            onItemClick(NEW_PATIENT_ID)
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //display delete icon on the menu bar if the user selected an item by checking it
        val menuId =
            if(this::adapter.isInitialized && adapter.selectedPatients.isNotEmpty()){
                R.menu.menu_main_selected
            }else{
                R.menu.menu_main
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sample_data -> addSampleData()
            R.id.action_sample_delete -> deleteSelectedPatients()
            //R.id.action_delete_all -> deleteAllNotes()
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun deleteSelectedPatients(): Boolean {
        viewModel.deletePatients(adapter.selectedPatients)
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedPatients.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
        return true
    }


    private fun addSampleData(): Boolean {
        viewModel.addSampleData()
        return true
    }


    override fun onItemClick(patientId: Int) {
        Log.i(TAG, "onItemClick: received patient id $patientId")
        val action = MainFragmentDirections.actionEditPatient(patientId)
        findNavController().navigate(action)
    }


    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }


}
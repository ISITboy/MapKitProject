package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.vehicleTypeManager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentVehicleTypeBinding


class VehicleTypeFragment : Fragment(R.layout.fragment_vehicle_type) {
    private lateinit var binding : FragmentVehicleTypeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVehicleTypeBinding.bind(view)
    }



}
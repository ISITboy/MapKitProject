package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mapkitresultproject.databinding.FragmentAddManagerStorageItemBinding
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.migration.CustomInjection.inject

@AndroidEntryPoint()
class AddManagerStorageItemFragment : BottomSheetDialogFragment()  {
    private lateinit var binding: FragmentAddManagerStorageItemBinding
    private val viewModel: StorageManagerViewModel by activityViewModels<StorageManagerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddManagerStorageItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED

        binding.imgClose.setOnClickListener {
            dismiss()
        }

        when(viewModel.event.value){
            ManagerEvent.AddShipperItem-> addShipper()
            ManagerEvent.AddConsigneeItem-> addConsignee()
        }
    }

    private fun addShipper()=with(binding){
        volumeCard.visibility = View.INVISIBLE
        btnSave.setOnClickListener {
            when(validateData(edtAddress.text.toString())){
                true -> showMessage("Address cannot be Empty!")
                false -> saveShipperItem(Shipper(address = edtAddress.text.toString()))
            }
        }
    }
    private fun saveShipperItem(shipper: Shipper) {
        viewModel.insertShipper(shipper)
        showMessage("Shipper saved")
        dismiss()
    }

    private fun addConsignee()= with(binding){
        btnSave.setOnClickListener {
            when(validateData(edtAddress.text.toString(),edtVolume.text.toString())){
                true -> showMessage("Address and Volume cannot be Empty!")
                false -> saveConsigneeItem(Consignee(
                    address = edtAddress.text.toString(),
                    volume = edtVolume.text.toString().toDouble()
                ))
            }
        }
    }

    private fun saveConsigneeItem(consignee: Consignee) {
        viewModel.insertConsignee(consignee)
        showMessage("Consignee saved")
        dismiss()
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun validateData(edtAddress: String, edtVolume: String = "data"): Boolean {
        return edtAddress.isEmpty() || edtVolume.isEmpty()
    }

}
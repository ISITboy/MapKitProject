package com.example.mapkitresultproject.presentation.tabs.profile.edit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentDetailsObjectBinding
import com.example.mapkitresultproject.databinding.FragmentEditProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileFragment : BottomSheetDialogFragment(R.layout.fragment_edit_profile) {
    lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        binding = FragmentEditProfileBinding.bind(view)
        Log.d("MyLog","onViewCreated")

        binding.apply {
            buttonEditProfile.setOnClickListener {
                if (editTextNameProfile.text!!.isNotEmpty() && editTextOrganizationProfile.text!!.isNotEmpty()) {
                    viewModel.updateUser(
                        editTextNameProfile.text.toString(),
                        editTextOrganizationProfile.text.toString()
                    )
                } else {
                    Toast.makeText(requireActivity(), "Введите данные", Toast.LENGTH_SHORT).show()
                }
                dismiss()
            }
        }
    }
}
package com.example.mapkitresultproject.presentation.tabs.map.details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils.goneOrRun
import com.example.mapkitresultproject.databinding.FragmentDetailsObjectBinding
import com.example.mapkitresultproject.domain.models.SelectedObjectHolder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DetailsFrag : BottomSheetDialogFragment(R.layout.fragment_details_object) {

    private lateinit var binding:FragmentDetailsObjectBinding
    private val viewModel: DetailsFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
        binding = FragmentDetailsObjectBinding.bind(view)
        viewModel.setSelectedGeoObject(SelectedObjectHolder.selectedObject)
//        parentFragmentManager.setFragmentResultListener(MapFragment.REQUEST_CODE, viewLifecycleOwner){_,data->
//            val geoObject = data. getParcelable<GeoObject>(MapFragment.EXTRA_GEOOBJECT)
//            viewModel.setSelectedGeoObject(geoObject)
//        }
        viewModel.uiState()?.let {
            binding.apply {
                textTitle.text = it.title
                textSubtitle.text = it.descriptionText
                textPlace.text = "${it.location?.latitude}, ${it.location?.longitude}"
                textUri.goneOrRun(it.uri) {
                    text = it
                }

                when (val state = it.typeSpecificState) {
                    is DetailsFragmentViewModel.TypeSpecificState.Business -> {
                        layoutBusinessInfo.isVisible = true
                        textType.text = "Business organisation:"
                        textBusinessName.text = state.name
                        textBusinessWorkingHours.goneOrRun(state.workingHours) {
                            text = it
                        }
                        textBusinessCategories.text = state.categories
                        textBusinessPhones.text = state.phones
                        textBusinessLinks.goneOrRun(state.link) {
                            text = it
                        }
                    }
                    is DetailsFragmentViewModel.TypeSpecificState.Toponym -> {
                        layoutToponymInfo.isVisible = true
                        textType.text = "Toponym:"
                        textToponymAddress.text = state.address
                    }
                    DetailsFragmentViewModel.TypeSpecificState.Undefined -> {
                        textType.isVisible = false
                    }
                }
            }
        }
    }


}
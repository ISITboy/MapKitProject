package com.example.mapkitresultproject.presentation.detailsscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils.goneOrRun
import com.example.mapkitresultproject.databinding.FragmentDetailsBinding

class DetailsFragment : DialogFragment() {

    private lateinit var binding: FragmentDetailsBinding

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState()?.let {
            binding.apply {
                textTitle.text = it.title
                textSubtitle.text = it.descriptionText
                textPlace.text = "${it.location?.latitude}, ${it.location?.longitude}"
                textUri.goneOrRun(it.uri) {
                    text = it
                }

                when (val state = it.typeSpecificState) {
                    is TypeSpecificState.Business -> {
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
                    is TypeSpecificState.Toponym -> {
                        layoutToponymInfo.isVisible = true
                        textType.text = "Toponym:"
                        textToponymAddress.text = state.address
                    }
                    TypeSpecificState.Undefined -> {
                        textType.isVisible = false
                    }
                }
            }
        }
    }


}
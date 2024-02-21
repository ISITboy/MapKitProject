package com.example.mapkitresultproject.presentation.tabs.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils.findTopNavController
import com.example.mapkitresultproject.databinding.FragmentProfileBinding
import com.example.mapkitresultproject.domain.state.RealtimeCRUDState
import com.example.mapkitresultproject.presentation.tabs.manager.ManagerFragmentViewModel
import com.example.mapkitresultproject.presentation.tabs.map.details.DetailsFrag
import com.example.mapkitresultproject.presentation.tabs.profile.edit.EditProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileFragmentViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        viewModel.readUser()

        viewModel.getState().flowWithLifecycle(lifecycle).onEach {state->
            when(state){
                is RealtimeCRUDState.Success  -> {
                    binding.cardProfile.findViewById<TextView>(R.id.nameTVProfile).text = state.user.name
                    binding.cardProfile.findViewById<TextView>(R.id.emailTVProfile).text = state.user.email
                    binding.cardProfile.findViewById<TextView>(R.id.organizationTVProfile).text = state.user.organization
                }
                is RealtimeCRUDState.Error -> Toast.makeText(requireActivity(),"${state.message}",
                    Toast.LENGTH_SHORT).show()
                else->{
                }
            }

        }.launchIn(lifecycleScope)


        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            title = getString(R.string.toolbar_title_profile)
            inflateMenu(R.menu.toolbar_profile_menu)

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.signOutProfileToolbar -> {
                        signOut()
                        true
                    }
                    else -> false
                }
            }
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.editButton.setOnClickListener {
            Log.d("MyLog","editButton")
            EditProfileFragment().show(childFragmentManager, EditProfileFragment().tag)
        }
    }

    private fun signOut() {
        Log.d("MyLog","toolbar signOut")
        viewModel.signOut()
        findTopNavController().navigate(R.id.signInFragment, null, navOptions {
            popUpTo(R.id.tabsFragment) {
                inclusive = true
            }
        })

    }
}


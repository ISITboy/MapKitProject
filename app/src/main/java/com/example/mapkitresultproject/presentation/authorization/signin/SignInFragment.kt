package com.example.mapkitresultproject.presentation.authorization.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentSignInBinding
import com.example.mapkitresultproject.domain.state.AuthResult
import com.example.mapkitresultproject.presentation.authorization.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentSignInBinding =
        { inflater, container ->
            FragmentSignInBinding.inflate(inflater, container, false)
        }

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputList = listOf(
            binding.authMail,
            binding.authPassword
        )

        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                AuthResult.Loading -> binding.progressBar.visibility = View.VISIBLE
                is AuthResult.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }

                is AuthResult.Success -> {
                    findNavController().navigate(R.id.action_signInFragment_to_tabsFragment,null,
                        navOptions {
                            popUpTo(R.id.signInFragment){
                                inclusive = true
                            }
                        })
                }
            }
        }

        binding.signIn.setOnClickListener {
            val allValidation = inputList.map { it.isValid() }

            if (allValidation.all { it }) {
                viewModel.sendCredentials(
                    email = binding.authMail.text(),
                    password = binding.authPassword.text()
                )
            }
        }
        binding.navigateToSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

    }
}
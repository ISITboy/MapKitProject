package com.example.mapkitresultproject.presentation.authorization.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.data.remote.Reference
import com.example.mapkitresultproject.databinding.FragmentSignInBinding
import com.example.mapkitresultproject.databinding.FragmentSignUpBinding
import com.example.mapkitresultproject.domain.models.User
import com.example.mapkitresultproject.domain.state.AuthResult
import com.example.mapkitresultproject.domain.state.RealtimeCRUDState
import com.example.mapkitresultproject.presentation.authorization.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>()  {
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentSignUpBinding =
        { inflater, container ->
            FragmentSignUpBinding.inflate(inflater, container, false)

        }
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputList = listOf(
            binding.signUpEmail,
            binding.signUpPasswordLayout
        )

        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                AuthResult.Loading -> binding.progressBarRegistration.visibility = View.VISIBLE
                is AuthResult.Error -> {
                    binding.progressBarRegistration.visibility = View.GONE
                    Toast.makeText(requireContext(), it.e.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }

                is AuthResult.Success -> {
                    viewModel.createUser(it.user as User.Base)
                    findNavController().popBackStack()
                }
            }
        }

        binding.startSignUp.setOnClickListener {
            val allValidation = inputList.map { it.isValid() }
            if (allValidation.all { it }) {
                viewModel.sendCredentials(
                    email = binding.signUpEmail.text(),
                    password = binding.signUpPasswordLayout.text()
                )
            }
        }
    }
}
//    private lateinit var binding: FragmentSignUpBinding
//    private val args by navArgs<SignUpFragmentArgs>()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentSignUpBinding.bind(view)
//
//        parentFragmentManager.setFragmentResultListener("R",viewLifecycleOwner){_,data->
//            val result = data.getInt("k")
//            Log.d("MyLog", "addresses33 = $result")
//
//        }
//
//
//    }
//
//    private fun getUserName() = args.username
//
//}
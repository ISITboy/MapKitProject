package com.example.mapkitresultproject.presentation.authorization.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentSignInBinding

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)

        parentFragmentManager.setFragmentResult("R", bundleOf("k" to 5))


        binding.buttonSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_tabsFragment,null, navOptions{
                popUpTo(R.id.signInFragment){
                    inclusive = true// все экраны, включая signInFragment будут удалены из стека
                }
            })
        }

        binding.buttonSignUp.setOnClickListener {
            val email = binding.textUserName.text.toString()
            val emailArg = if (email.isBlank())
                null
            else {
                email
            }
            val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(emailArg)
            findNavController().navigate(direction)
        }
    }
}
package com.example.mapkitresultproject.presentation.authorization.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentSignInBinding
import com.example.mapkitresultproject.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private val args by navArgs<SignUpFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignUpBinding.bind(view)

        parentFragmentManager.setFragmentResultListener("R",viewLifecycleOwner){_,data->
            val result = data.getInt("k")
            Log.d("MyLog", "addresses33 = $result")

        }


    }

    private fun getUserName() = args.username

}
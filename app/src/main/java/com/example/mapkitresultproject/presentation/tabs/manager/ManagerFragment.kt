package com.example.mapkitresultproject.presentation.tabs.manager

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils.findTopNavController
import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.databinding.FragmentManagerBinding
import com.example.mapkitresultproject.presentation.tabs.map.MapViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ManagerFragment : Fragment(R.layout.fragment_manager) {

    private lateinit var binding: FragmentManagerBinding
    private val viewModel: ManagerFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentManagerBinding.bind(view)

        requireActivity().supportFragmentManager.setFragmentResultListener("RR",viewLifecycleOwner){_,data->
            data.getInt("c")?.let {
                viewModel.count = it
            }
            data.getDoubleArray("k")?.let {
                viewModel.setAddresses(it.toList())
            }
//            Log.d("MyLog", "result = ${result?.toList().toString()}")
        }

        viewModel.addresses.observe(requireActivity()){
            sendDataToServer(it)
        }
        viewModel.getDistances.observe(requireActivity()){
            Log.d(ManagerFragmentViewModel.TAG,"run")
            viewModel.startClarkeRightAlgo(it, viewModel.count)
        }
        binding.click.setOnClickListener {
            findTopNavController().navigate(R.id.detailsManagerFragment)
        }
    }

    private fun sendDataToServer(points: List<Double>) {
        val list = mutableListOf<List<Double>>()
        for (item in points.indices step 2){
            val currentList = listOf(points[item], points[item+1])
            list.add(currentList)
        }
        viewModel.calculateDistance(
            Query(
                locations = list,
                metrics = listOf("distance")
            )
        )
    }

    fun readMatrix(matrix: List<List<Double>>) {
        Log.d("MyLog", "matrix LtnLtd")
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                //Log.d("MyLog","${matrix[i][j]} \t")
                print("${matrix[i][j]} \t")
            }
            println()
        }
    }
}
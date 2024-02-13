package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentDetailsManagerBinding
import com.example.mapkitresultproject.domain.models.SearchOptionBuilder
import com.example.mapkitresultproject.domain.state.SearchState
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.adapter.ConsigneeItemsAdapter
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.adapter.ShipperItemsAdapter
import com.yandex.mapkit.search.SearchOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.LinkedList
import java.util.Queue


@AndroidEntryPoint
class DetailsManagerFragment : Fragment(R.layout.fragment_details_manager) {

    lateinit var binding: FragmentDetailsManagerBinding
    private val viewModel: StorageManagerViewModel by activityViewModels()
    private val shipperItemAdapter by lazy { ShipperItemsAdapter(viewModel) }
    private val consigneeItemAdapter by lazy { ConsigneeItemsAdapter(viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsManagerBinding.bind(view)

        initRecyclerViews()
        initItemTouchHelper()
        viewModel.getAllShipperItems()
        viewModel.getAllConsigneeItems()

        viewModel.getShipper().observe(requireActivity(), Observer {
//            showMessage(it.isEmpty())
            shipperItemAdapter.updateAdapter(it)
            Log.d("MyLog", "sizeStorageManager: ${it.size}")
        })
        viewModel.getConsignee().observe(requireActivity(), Observer {
//            showMessage(it.isEmpty())
            consigneeItemAdapter.updateAdapter(it)
        })

        binding.addConsigneesButton.setOnClickListener {
            viewModel.event.value = ManagerEvent.AddConsigneeItem
            Log.d("MyLog", "value = ${viewModel.event.value}")
            AddManagerStorageItemFragment().show(
                childFragmentManager,
                AddManagerStorageItemFragment().tag
            )
        }
        binding.addShipperButton.setOnClickListener {
            viewModel.event.value = ManagerEvent.AddShipperItem
            AddManagerStorageItemFragment().show(
                childFragmentManager,
                AddManagerStorageItemFragment().tag
            )
        }
        viewModel.setVisibleRegion()
        viewModel.setSearchOption(SearchOptionBuilder().build().setResultPageSize(1))

        val addressed = mutableListOf<Double>()

        viewModel.getSearchState().flowWithLifecycle(lifecycle).onEach { state ->

            if (state is SearchState.Success) {
                val successSearchState = state as? SearchState.Success
                val searchItems = successSearchState?.items ?: emptyList()
                if (searchItems.isNotEmpty()) {
                    addressed.add(searchItems.first().point.longitude)
                    addressed.add(searchItems.first().point.latitude)

                    if (addressed.size == (viewModel.getConsignee().value!!.size + viewModel.getShipper().value!!.size) * 2) {
                        requireActivity().supportFragmentManager.setFragmentResult("RR", bundleOf("k" to addressed.toDoubleArray()))
                        findNavController().popBackStack()
                    }
                }
            }

        }.launchIn(lifecycleScope)

        binding.startButton.setOnClickListener {
            val shippers = viewModel.getShipper().value?.map { it.address } ?: emptyList()
            val consignees = viewModel.getConsignee().value?.map { it.address } ?: emptyList()
            val addresses = shippers + consignees
            val items: Queue<String> = LinkedList<String>(addresses)
            viewModel.createSession(items)
        }
    }


    private fun initRecyclerViews() = with(binding) {
        shipperRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = shipperItemAdapter
        }
        consigneeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = consigneeItemAdapter
        }
    }


    private fun initItemTouchHelper() = with(binding) {
        val swapHelper1 = ItemTouchHelper(itemTouchCallback())
        swapHelper1.attachToRecyclerView(consigneeRecyclerView)
        val swapHelper2 = ItemTouchHelper(itemTouchCallback())
        swapHelper2.attachToRecyclerView(shipperRecyclerView)

    }


    private fun itemTouchCallback() = object : ItemTouchHelper.SimpleCallback(
        0, // dragDirs - не используется для удаления, поэтому 0
        ItemTouchHelper.RIGHT // swipeDirs - разрешаем удаление свайпом влево и вправо
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            findRecyclerViewFromViewHolder(viewHolder)
//                showMessage("Note deleted")
        }

    }

    private fun findRecyclerViewFromViewHolder(viewHolder: RecyclerView.ViewHolder) {
        // Проверяем тип ViewHolder или другие параметры, чтобы определить `RecyclerView`

        // Например, если у вас есть два разных типа ViewHolder:
        if (viewHolder is ConsigneeItemsAdapter.MyHolder) {
            // Этот ViewHolder относится к первому RecyclerView
            consigneeItemAdapter.removeItem(viewHolder.adapterPosition)
        } else if (viewHolder is ShipperItemsAdapter.MyHolder) {
            // Этот ViewHolder относится ко второму RecyclerView
            shipperItemAdapter.removeItem(viewHolder.adapterPosition)
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun checkMaxValueBus(): Boolean {
        return binding.inputLimitBus.text.toString().isEmpty()
    }
}
package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.Surface
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentDetailsManagerBinding
import com.example.mapkitresultproject.domain.models.SearchOptionBuilder
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.state.SearchRouteState
import com.example.mapkitresultproject.domain.state.SearchState
import com.example.mapkitresultproject.presentation.basecomponent.mapkit.MapKitFragment
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.compose.DetailsManagerCompose
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.LinkedList
import java.util.Queue


@AndroidEntryPoint
class DetailsManagerFragment : MapKitFragment<FragmentDetailsManagerBinding>() {
    private val viewModel: StorageManagerViewModel by activityViewModels()
    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentDetailsManagerBinding
        get() = { inflater, container ->
            FragmentDetailsManagerBinding.inflate(inflater, container, false)
        }
    override val searchState: MutableStateFlow<SearchState>
        get() = viewModel.getSearchState()
    override val subscribeForSearch: Flow<*>?
        get() = null
    override val searchRouteState: MutableStateFlow<SearchRouteState>?
        get() = null


    private val addressed = mutableListOf<Double>()
    private lateinit var composeContainer: ComposeView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeContainer = view.findViewById(R.id.composeContainer)
        composeContainer.setContent {
            val providers = viewModel.getShipper().observeAsState()
            val consignee = viewModel.getConsignee().observeAsState()
            val transports = viewModel.getTransports().observeAsState()
            if (providers.value != null && consignee.value != null && transports.value!=null) {
                DetailsManagerCompose(
                    providers = providers.value!!,
                    consumers = consignee.value!!,
                    transports = transports.value!!,
                    event = viewModel::eventManager,
                    makeRoute = {
                        val shippers = viewModel.getShipper().value?.map { it.address } ?: emptyList()
                        val consignees = viewModel.getConsignee().value?.map { it.address } ?: emptyList()
                        val addresses = shippers + consignees
                        val items: Queue<String> = LinkedList(addresses)
                        viewModel.createSearchSession(items)
                    }
                )
            }
        }
        viewModel.getAllShipperItems()
        viewModel.getAllConsigneeItems()
        viewModel.getAllTransportItems()
        viewModel.setVisibleRegion(null)
        viewModel.setSearchOption(SearchOptionBuilder().build().setResultPageSize(1))
    }

    override fun <T> actionWithStateError(state: T) {

    }

    override fun <T> actionWithStateLoading(state: T) {

    }

    override fun <T> actionWithStateSuccess(state: T) {
        when (state) {
            is SearchState.Success -> {
                val successSearchState = state as? SearchState.Success
                val searchItems = successSearchState?.items ?: emptyList()
                addressed.add(searchItems.first().point.longitude)
                addressed.add(searchItems.first().point.latitude)
                if (addressed.size == (viewModel.getConsignee().value!!.size + viewModel.getShipper().value!!.size) * 2) {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        "RR",
                        bundleOf(
                            "k" to addressed.toDoubleArray(),
                            "c" to viewModel.getShipper().value
                        )
                    )
                    findNavController().popBackStack()
                }
            }
            is SearchRouteState.Success -> {}
        }
    }
}
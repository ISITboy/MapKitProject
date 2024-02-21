package com.example.mapkitresultproject.presentation.basecomponent.mapkit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.mapkitresultproject.domain.state.SearchRouteState
import com.example.mapkitresultproject.domain.state.SearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class MapKitFragment <B : ViewBinding> : Fragment() {
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?) -> B
    private var _binding: B? = null
    protected val binding get() = _binding!!
    protected abstract val searchState: MutableStateFlow<SearchState>
    protected abstract val subscribeForSearch : Flow<*>?
    protected abstract val searchRouteState:MutableStateFlow<SearchRouteState>?

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater.invoke(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchState.flowWithLifecycle(lifecycle)
            .onEach { state ->
                actionWithStateLoading(state = state)

                if (state is SearchState.Error) actionWithStateError(state)
                else {
                    actionWithStateSuccess(state)
                }
            }.launchIn(lifecycleScope)
        subscribeForSearch?.flowWithLifecycle(lifecycle)?.launchIn(lifecycleScope)

        searchRouteState?.flowWithLifecycle(lifecycle)
            ?.onEach { state ->
                actionWithStateLoading(state = state)

                if (state is SearchRouteState.Error) actionWithStateError(state)
                else {
                    actionWithStateSuccess(state)
                }

            }?.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun <T> actionWithStateError(state: T)

    abstract fun <T> actionWithStateLoading(state: T)

    abstract fun <T> actionWithStateSuccess(state: T)
}
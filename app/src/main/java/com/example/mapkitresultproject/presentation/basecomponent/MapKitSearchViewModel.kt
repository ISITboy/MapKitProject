package com.example.mapkitresultproject.presentation.basecomponent

import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.domain.state.SearchState
import com.example.mapkitresultproject.domain.usecase.mapkitusescases.SearchUseCase
import com.yandex.mapkit.search.SearchOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Queue
import javax.inject.Inject

@AndroidEntryPoint
abstract class MapKitSearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    abstract fun setSearchOption(searchOptions: SearchOptions)
    abstract fun createSearchSession(query:String)
    abstract fun createSession(query: Queue<String>)
    abstract fun setVisibleRegion()
    abstract fun getSearchState() : MutableStateFlow<SearchState>
}
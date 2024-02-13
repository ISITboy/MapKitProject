package com.example.mapkitresultproject.presentation.tabs.map.details

import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.Utils.takeIfNotEmpty
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.mapkit.uri.UriObjectMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsFragmentViewModel @Inject constructor() : ViewModel() {
    private var selectedObject: GeoObject? = null

    fun setSelectedGeoObject(selectedObject: GeoObject?) {
        this.selectedObject = selectedObject
    }

    private fun getUri() =
        selectedObject?.metadataContainer?.getItem(UriObjectMetadata::class.java)?.uris?.firstOrNull()

    fun uiState(): DetailsDialogUiState? {
        if (selectedObject == null) return null
        val geoObjetTypeUiState = selectedObject?.metadataContainer?.getItem(ToponymObjectMetadata::class.java)?.let {
            TypeSpecificState.Toponym(address = it.address.formattedAddress)
        } ?: selectedObject?.metadataContainer?.getItem(BusinessObjectMetadata::class.java)?.let {
            TypeSpecificState.Business(
                name = it.name,
                workingHours = it.workingHours?.text,
                categories = it.categories.map { it.name }.takeIfNotEmpty()?.toSet()
                    ?.joinToString(", "),
                phones = it.phones.map { it.formattedNumber }.takeIfNotEmpty()?.joinToString(", "),
                link = it.links.firstOrNull()?.link?.href,
            )
        } ?: TypeSpecificState.Undefined

        return DetailsDialogUiState(
            title = selectedObject?.name ?: "No title",
            descriptionText = selectedObject?.descriptionText ?: "No description",
            location = selectedObject?.geometry?.firstOrNull()?.point,
            uri = getUri()?.value,
            typeSpecificState = geoObjetTypeUiState,
        )
    }

    data class DetailsDialogUiState(
        val title: String,
        val descriptionText: String,
        val location: Point?,
        val uri: String?,
        val typeSpecificState: TypeSpecificState,
    )

    sealed interface TypeSpecificState {
        data class Toponym(val address: String) : TypeSpecificState

        data class Business(
            val name: String,
            val workingHours: String?,
            val categories: String?,
            val phones: String?,
            val link: String?,
        ) : TypeSpecificState

        object Undefined : TypeSpecificState
    }
}
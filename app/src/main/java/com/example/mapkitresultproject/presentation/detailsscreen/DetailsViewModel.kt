package com.example.mapkitresultproject.presentation.detailsscreen

import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.Utils.takeIfNotEmpty
import com.example.mapkitresultproject.domain.models.SelectedObjectHolder
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.mapkit.uri.UriObjectMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
@HiltViewModel
class DetailsViewModel @Inject constructor() : ViewModel() {

    fun uiState(): DetailsDialogUiState? {
        val geoObject = SelectedObjectHolder.selectedObject ?: return null
        val uri = geoObject.metadataContainer.getItem(UriObjectMetadata::class.java).uris.firstOrNull()

        val geoObjetTypeUiState = geoObject.metadataContainer.getItem(ToponymObjectMetadata::class.java)?.let {
            TypeSpecificState.Toponym(address = it.address.formattedAddress)
        } ?: geoObject.metadataContainer.getItem(BusinessObjectMetadata::class.java)?.let {
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
            title = geoObject.name ?: "No title",
            descriptionText = geoObject.descriptionText ?: "No description",
            location = geoObject.geometry.firstOrNull()?.point,
            uri = uri?.value,
            typeSpecificState = geoObjetTypeUiState,
        )
    }
}

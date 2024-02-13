package com.example.mapkitresultproject.presentation.tabs.map

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.databinding.FragmentMapBinding
import com.example.mapkitresultproject.domain.models.CameraPositionBuilder
import com.example.mapkitresultproject.domain.models.SearchRouteState
import com.example.mapkitresultproject.domain.models.SearchState
import com.example.mapkitresultproject.domain.models.SelectedObjectHolder
import com.example.mapkitresultproject.presentation.tabs.map.details.DetailsFrag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.search.SearchType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MapFragment : Fragment() {


    lateinit var binding: FragmentMapBinding
    private val viewModel: MapViewModel by viewModels()
    private lateinit var animation: com.example.mapkitresultproject.presentation.tabs.map.Animation


    private lateinit var editQueryTextWatcher: TextWatcher

    private val cameraListener = CameraListener { _, _, finished, _ ->
        // Updating current visible region to apply research on map moved by user gestures.
        if (finished == CameraUpdateReason.GESTURES) {
            when (binding.searchText.text.isEmpty()) {
                false -> viewModel.setVisibleRegion(viewModel.map.visibleRegion)
                true -> viewModel.setVisibleRegion(null)
            }
            updateFocusRect()
        }
    }

    private val searchResultPlacemarkTapListener = MapObjectTapListener { mapObject, _ ->
        // Show details dialog on placemark tap.
        val selectedObject = (mapObject.userData as? GeoObject)
//        parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(EXTRA_GEOOBJECT to selectedObject))
        SelectedObjectHolder.selectedObject = selectedObject
        DetailsFrag().show(childFragmentManager, DetailsFrag().tag)
        true
    }


    private val routeTapListener = MapObjectTapListener { mapObject, p1 ->

        viewModel.getPolylinesMapsObjects().forEach {
            it?.setStrokeColor(ContextCompat.getColor(requireActivity(), R.color.gray))
        }

        val polyline = (mapObject as PolylineMapObject)
        polyline.setStrokeColor(ContextCompat.getColor(requireActivity(), R.color.light_blue))

        val selectedObject = (mapObject.userData as DrivingRoute)
        setDataForInfoRoute(drivingRoute = selectedObject)

        if (binding.slidingLayout.visibility == View.VISIBLE) {
            hideSlidingLayout()
            showSlidingLayout()
        } else {
            showSlidingLayout()
        }
        true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        animation = Animation(requireActivity())
        viewModel.map = binding.mapview.map
        viewModel.map.addCameraListener(cameraListener)
        setDependencyForSearchPoint()
        setDependencyForCreateRoute()
        setDependencyForInteractionMap()
        moveToStartLocation()

        viewModel.getSearchState().flowWithLifecycle(lifecycle)
            .onEach { state ->
                actionWithStateLoading(state = state)

                if (state is SearchState.Error) actionWithStateError(state)
                else {
                    hideErrorMessageLayout()
                    val successSearchState = state as? SearchState.Success
                    val searchItems = successSearchState?.items ?: emptyList()
                    if (successSearchState?.zoomToItems == true) {
                        viewModel.focusCamera(
                            searchItems.map { item -> item.point },
                            successSearchState.itemsBoundingBox
                        )
                    }
                }

            }.launchIn(lifecycleScope)

        viewModel.subscribeForSearch().flowWithLifecycle(lifecycle).launchIn(lifecycleScope)

        binding.searchText.setOnEditorActionListener { _, _, _ ->
            viewModel.setVisibleRegion(viewModel.map.visibleRegion)
            viewModel.createSession(
                binding.searchText.text.toString()
            )
            true
        }

        viewModel.getCreateRouteState().flowWithLifecycle(lifecycle)
            .onEach { state ->
                actionWithStateLoading(state = state)

                if (state is SearchRouteState.Error) actionWithStateError(state)
                else {
                    hideErrorMessageLayout()
                    val successSearchRouteState = state as? SearchRouteState.Success
                    val searchItems = successSearchRouteState?.drivingRoutes ?: emptyList()
                    viewModel.onRoutesUpdated(viewModel.map, searchItems)
                    searchItems.forEach { item ->
                        viewModel.focusCamera(
                            item.geometry.points,
                            Polyline(item.geometry.points)
                        )
                    }
                }

            }.launchIn(lifecycleScope)

        viewModel.getSelectedPoint().observe(requireActivity()) {
            viewModel.setPointForRoute(it)
            viewModel.createSessionCreateRoute()
        }

        binding.layoutButtons.findViewById<FloatingActionButton>(R.id.floatingActionButtonClearPoint)
            .setOnClickListener {
                binding.searchText.text.clear()
                binding.searchText.isCursorVisible = false
                viewModel.setVisibleRegion(null)
                viewModel.clearObjectCollection()
            }
        binding.layoutButtons.findViewById<FloatingActionButton>(R.id.floatingActionButtonClearRoad)
            .setOnClickListener {
                viewModel.clearRoutes()
                viewModel.clearSelectedPointsForCreateRoute()
            }

        binding.slidingLayout.setOnClickListener {
            hideSlidingLayout()
            viewModel.getPolylinesMapsObjects().forEach {
                if (it?.isValid == true) it.setStrokeColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.gray
                    )
                )
            }
        }
        binding.searchButton.setOnClickListener {
            viewModel.setVisibleRegion(viewModel.map.visibleRegion)
            viewModel.createSession(
                binding.searchText.text.toString()
            )
        }
    }

    private fun setDependencyForSearchPoint() {
        viewModel.setMapObjectTapListener(searchResultPlacemarkTapListener)
        viewModel.setMapObjectCollection(viewModel.map.mapObjects)
        viewModel.setSearchOption(15, SearchType.NONE)
    }

    private fun setDependencyForCreateRoute() {
        viewModel.setRouteTapListener(routeTapListener)
        viewModel.setMapObjectRoutesCollection(viewModel.map.mapObjects)
        viewModel.setDrivingOptions(1)
        viewModel.setVehicleOptions(VehicleType.TRUCK, 5000f)
    }

    private fun setDependencyForInteractionMap() {
        viewModel.addMapInputListener(viewModel.map)
        viewModel.setMapObjectPlacemarksCollection(viewModel.map.mapObjects)
    }


    private fun updateFocusRect() {
        val horizontal = resources.getDimension(R.dimen.window_horizontal_padding)
        val vertical = resources.getDimension(R.dimen.window_vertical_padding)
        val window = binding.mapview.mapWindow

        window.focusRect = ScreenRect(
            ScreenPoint(horizontal, vertical),
            ScreenPoint(window.width() - horizontal, window.height() - vertical),
        )
    }

    private fun moveToStartLocation() {
        val startCameraPosition = CameraPositionBuilder().build()
        binding.mapview.map.move(
            startCameraPosition,
            Animation(Animation.Type.SMOOTH, 2f), null
        )
    }



    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


    private fun showSlidingLayout() = with(binding) {
        slidingLayout.visibility = View.VISIBLE
        slidingLayout.startAnimation(animation.routeInfoAnim.slideIn)
    }

    private fun hideSlidingLayout() = with(binding) {
        slidingLayout.startAnimation(animation.routeInfoAnim.slideOut)
        slidingLayout.visibility = View.GONE
    }

    private fun showErrorMessageLayout() = with(binding) {
        errorMessageLayout.visibility = View.VISIBLE
        errorMessageLayout.startAnimation(animation.errorMessageAnim.slideIn)
    }

    private fun hideErrorMessageLayout() = with(binding) {
//        errorMessageLayout.startAnimation(animation.errorMessageAnim.slideOut)
        errorMessageLayout.visibility = View.GONE
    }

    private fun <T> actionWithStateError(state: T) = with(binding) {
        when (state) {
            is SearchState.Error -> {
                val error = state.message
                errorMessageLayout.findViewById<TextView>(R.id.errorMessageTextView).text = error
                showErrorMessageLayout()
            }

            is SearchRouteState.Error -> {
                val error = (state as SearchRouteState.Error).message
                errorMessageLayout.findViewById<TextView>(R.id.errorMessageTextView).text = error
                showErrorMessageLayout()
            }
        }
    }

    private fun <T> actionWithStateLoading(state: T) = with(binding) {

        when (state is SearchState.Loading || state is SearchRouteState.Loading) {
            true -> {
                if (state is SearchState) searchButton.isEnabled = false
                startLoadingIndicatorAnimations()
            }

            false -> {
                if (state is SearchState) searchButton.isEnabled = true
                stopLoadingIndicatorAnimations()
            }
        }
    }

    private fun startLoadingIndicatorAnimations() {
        binding.loadingIndicator.visibility = View.VISIBLE
        renderAnimations(initialAlpha = 0f, finalAlpha = 1f)
    }

    private fun stopLoadingIndicatorAnimations() {
        renderAnimations(initialAlpha = 1f, finalAlpha = 0f)
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun renderAnimations(initialAlpha: Float, finalAlpha: Float) {
        binding.loadingIndicator.alpha = initialAlpha
        binding.loadingIndicator.animate()
            .alpha(finalAlpha)
            .setDuration(500)
            .start()
    }

    private fun setDataForInfoRoute(drivingRoute: DrivingRoute) {
        val distance = drivingRoute.metadata.weight.distance.text
        val time = drivingRoute.metadata.weight.time.text
        val railwayCrossingsSize = drivingRoute.railwayCrossings.size
        val pedestrianCrossingsSize = drivingRoute.pedestrianCrossings.size
        val speedBumpsSize = drivingRoute.speedBumps.size
        binding.slidingLayout.findViewById<TextView>(R.id.distanceTextView).text = distance
        binding.slidingLayout.findViewById<TextView>(R.id.timeTextView).text = time
        binding.slidingLayout.findViewById<TextView>(R.id.railwayCrossingsTextView).text =
            railwayCrossingsSize.toString()
        binding.slidingLayout.findViewById<TextView>(R.id.pedestrianCrossingsTextView).text =
            pedestrianCrossingsSize.toString()
        binding.slidingLayout.findViewById<TextView>(R.id.speedBumpsTextView).text =
            speedBumpsSize.toString()
    }


    companion object {
        const val REQUEST_CODE = "mapObject"
        const val EXTRA_GEOOBJECT = "GeoObject"
    }
}
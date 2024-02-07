package com.example.mapkitresultproject.presentation.mapscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils
import com.example.mapkitresultproject.databinding.ActivityMainBinding
import com.example.mapkitresultproject.databinding.CardRouteInfoBinding
import com.example.mapkitresultproject.domain.models.SearchRouteState
import com.example.mapkitresultproject.domain.models.SearchState
import com.example.mapkitresultproject.domain.models.SelectedObjectHolder
import com.example.mapkitresultproject.domain.models.SelectedRouteHolder
import com.example.mapkitresultproject.domain.models.SelectedRouteHolder.selectedRoute
import com.example.mapkitresultproject.domain.models.StartLocation
import com.example.mapkitresultproject.presentation.detailsscreen.DetailsFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.SearchType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MapViewModel by viewModels()


    private lateinit var editQueryTextWatcher: TextWatcher

    private val cameraListener = CameraListener { _, _, finished, _ ->
        // Updating current visible region to apply research on map moved by user gestures.
        if (finished == CameraUpdateReason.GESTURES) {
            when(binding.searchText.text.isEmpty()){
                false -> viewModel.setVisibleRegion(viewModel.map.visibleRegion)
                true -> viewModel.setVisibleRegion(null)
            }

            updateFocusRect()
        }
    }

    private val searchResultPlacemarkTapListener = MapObjectTapListener { mapObject, _ ->
        // Show details dialog on placemark tap.
        val selectedObject = (mapObject.userData as? GeoObject)
        SelectedObjectHolder.selectedObject = selectedObject
        DetailsFragment().show(supportFragmentManager, null)
        true
    }

    private val routeTapListener = MapObjectTapListener { mapObject, p1 ->
        selectedRoute.forEach {
            it?.setStrokeColor(ContextCompat.getColor(this, R.color.gray))
        }

        val polyline = (mapObject as PolylineMapObject)
        polyline.setStrokeColor(ContextCompat.getColor(this, R.color.light_blue))
        val selectedObject = (mapObject.userData as DrivingRoute)

        val distance = selectedObject.metadata.weight.distance.text
        val time = selectedObject.metadata.weight.time.text
        val railwayCrossingsSize = selectedObject.railwayCrossings.size
        val pedestrianCrossingsSize = selectedObject.pedestrianCrossings.size
        val speedBumpsSize = selectedObject.speedBumps.size
        binding.slidingLayout.findViewById<TextView>(R.id.distanceTextView).text = distance
        binding.slidingLayout.findViewById<TextView>(R.id.timeTextView).text = time
        binding.slidingLayout.findViewById<TextView>(R.id.railwayCrossingsTextView).text = railwayCrossingsSize.toString()
        binding.slidingLayout.findViewById<TextView>(R.id.pedestrianCrossingsTextView).text = pedestrianCrossingsSize.toString()
        binding.slidingLayout.findViewById<TextView>(R.id.speedBumpsTextView).text = speedBumpsSize.toString()

        if (binding.slidingLayout.visibility == View.VISIBLE) {
            hideSlidingLayout()
            showSlidingLayout()
        }else{
            showSlidingLayout()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.map = binding.mapview.map
        viewModel.setVisibleRegion(viewModel.map.visibleRegion)
        viewModel.addMapInputListener(viewModel.map)

        viewModel.map.addCameraListener(cameraListener)


        viewModel.setMapObjectTapListener(searchResultPlacemarkTapListener)
        viewModel.setRouteTapListener(routeTapListener)

        viewModel.setMapObjectCollection(viewModel.map.mapObjects)
        viewModel.setMapObjectRoutesCollection(viewModel.map.mapObjects)
        viewModel.setMapObjectPlacemarksCollection(viewModel.map.mapObjects)

        moveToStartLocation(StartLocation())

        viewModel.setSearchOption(15, SearchType.NONE)

        viewModel.getSearchState().flowWithLifecycle(lifecycle)
            .onEach {state->

                if (state !is SearchState.Error){
                    val successSearchState = state as? SearchState.Success
                    val searchItems = successSearchState?.items ?: emptyList()
                    if (successSearchState?.zoomToItems == true) {
                        viewModel.focusCamera(
                            searchItems.map { item -> item.point },
                            successSearchState.itemsBoundingBox
                        )
                    }
                }else{
                    Log.d("MyLog","Error message ${state.message}")
                }

                binding.apply {
                    when(state is SearchState.Loading){
                        true -> searchButton.isEnabled = false
                        false-> searchButton.isEnabled = true
                    }
                }

            }.launchIn(lifecycleScope)
        viewModel.subscribeForSearch().flowWithLifecycle(lifecycle).launchIn(lifecycleScope)


        binding.searchText.setOnEditorActionListener { _, _, _ ->
            viewModel.createSession(
                binding.searchText.text.toString())
            true
        }

        viewModel.setDrivingOptions(1)
        viewModel.setVehicleOptions(VehicleType.TRUCK, 5000f)

        viewModel.getCreateRouteState().flowWithLifecycle(lifecycle)
            .onEach {
                val successSearchRouteState = it as? SearchRouteState.Success
                val searchItems = successSearchRouteState?.drivingRoutes ?: emptyList()
                viewModel.onRoutesUpdated(viewModel.map,searchItems)
                searchItems.forEach {item->
                    viewModel.focusCamera(
                        item.geometry.points,
                        Polyline(item.geometry.points)
                    )
                }
            }.launchIn(lifecycleScope)


        viewModel.getSelectedPoint().observe(this) {
            viewModel.setPointForRoute(it)
            viewModel.createSessionCreateRoute()
        }


        binding.layoutButtons.findViewById<FloatingActionButton>(R.id.floatingActionButtonClearAllObject).setOnClickListener {
//            selectedRoute.clear()
//            viewModel.map.mapObjects.clear()
        }
        binding.layoutButtons.findViewById<FloatingActionButton>(R.id.floatingActionButtonClearPoint).setOnClickListener {
            binding.searchText.text.clear()
            binding.searchText.isCursorVisible = false
            viewModel.setVisibleRegion(null)
            viewModel.clearObjectCollection()
        }
        binding.layoutButtons.findViewById<FloatingActionButton>(R.id.floatingActionButtonClearRoad).setOnClickListener {
            viewModel.clearPointsForRoute()
            viewModel.clearSelectedPointsForCreateRoute()
        }

        binding.slidingLayout.setOnClickListener {
            hideSlidingLayout()
            selectedRoute.forEach {
                if(it?.isValid==true) it.setStrokeColor(ContextCompat.getColor(this, R.color.gray))
            }
        }
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

    private fun moveToStartLocation(startLocation: StartLocation) =
        with(startLocation) {
            binding.mapview.map.move(
                CameraPosition(location, zoomValue, azimuthValue, tiltValue),
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

    fun onFindClickButton(view: View) {
        viewModel.createSession(
            binding.searchText.text.toString())
    }


    private fun showSlidingLayout() = with(binding) {
        slidingLayout.visibility = View.VISIBLE
        val slideInAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_in)
        slidingLayout.startAnimation(slideInAnimation)
    }

    private fun hideSlidingLayout() = with(binding){
        val slideOutAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_out)
        slideOutAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {

            }
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                slidingLayout.visibility = View.GONE
            }
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {
            }
        })
        slidingLayout.startAnimation(slideOutAnimation)
    }

}

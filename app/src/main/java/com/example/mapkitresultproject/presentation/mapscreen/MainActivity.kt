package com.example.mapkitresultproject.presentation.mapscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils
import com.example.mapkitresultproject.databinding.ActivityMainBinding
import com.example.mapkitresultproject.domain.models.SearchState
import com.example.mapkitresultproject.domain.models.SelectedObjectHolder
import com.example.mapkitresultproject.presentation.detailsscreen.DetailsFragment
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
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.SearchType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.flow
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

        moveToStartLocation(StartLocation())

        viewModel.setSearchOption(15, SearchType.NONE)


//        viewModel.getResultedPoint().observe(this) {
//            Log.d("MyLog", "point: ${it.longitude} ${it.latitude}")
//            focusCamera(listOf(it), viewModel.map)
//        }

        viewModel.getSearchState().flowWithLifecycle(lifecycle)
            .onEach {
                val successSearchState = it as? SearchState.Success
                val searchItems = successSearchState?.items ?: emptyList()
                if (successSearchState?.zoomToItems == true) {
                    focusCamera(
                        searchItems.map { item -> item.point },
                        successSearchState.itemsBoundingBox
                    )
                }
            }.launchIn(lifecycleScope)

        viewModel.subscribeForSearch().flowWithLifecycle(lifecycle).launchIn(lifecycleScope)


        binding.searchText.setOnEditorActionListener { _, _, _ ->
            viewModel.createSession(
                binding.searchText.text.toString(),
                VisibleRegionUtils.toPolygon(binding.mapview.map.visibleRegion)
            )
            true
        }

        viewModel.setMapObjectCollection__(viewModel.map.mapObjects)


        viewModel.setMapObjectCollection(viewModel.map.mapObjects)
        viewModel.setDrivingOptions(1)
        viewModel.setVehicleOptions(VehicleType.TRUCK, 5000f)
//        viewModel.getResultedRout().observe(this) {
//            viewModel.onRoutesUpdated(it)
//            focusCamera(it.flatMap { it.geometry.points }, viewModel.map)
//        }

        viewModel.setMapObjectCollection_(viewModel.map.mapObjects)


        viewModel.getResultedPoints().observe(this) {
            Log.d("MyLog", "observe ${Utils.getCoordinates(it)}")
            viewModel.setPointForRoute(it)
            viewModel.createSessionCreateRoute()
        }



    }

    private fun focusCamera(points: List<Point>, boundingBox: BoundingBox) {
        if (points.isEmpty()) return

        val position = if (points.size == 1) {
            viewModel.map.cameraPosition.run {
                CameraPosition(points.first(), zoom, azimuth, tilt)
            }
        } else {
            viewModel.map.cameraPosition(Geometry.fromBoundingBox(boundingBox))
        }

        viewModel.map.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)
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

    data class StartLocation(
        val location: Point = Point(53.685274, 23.837227),
        val zoomValue: Float = 10.0f,//Уровень масштабирования.
        val azimuthValue: Float = 0.0f,//Угол между севером и интересующим направлением на плоскости карты, в градусах в диапазоне [0, 360).
        val tiltValue: Float = 0f//Наклон камеры в градусах.
    )

    fun onFindClickButton(view: View) {
        when (viewModel.getSearchState().value) {
            SearchState.Off -> {
                viewModel.createSession(
                    binding.searchText.text.toString(),
                    VisibleRegionUtils.toPolygon(binding.mapview.map.visibleRegion)
                )
            }
            SearchState.Loading -> binding.searchButton.isEnabled = false
            SearchState.Error() -> Toast.makeText(
                this,
                (viewModel.getSearchState() as SearchState.Error).message,
                Toast.LENGTH_SHORT
            ).show()
            else->{

            }
        }
    }
}
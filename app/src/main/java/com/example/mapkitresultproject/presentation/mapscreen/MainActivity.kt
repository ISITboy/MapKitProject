package com.example.mapkitresultproject.presentation.mapscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mapkitresultproject.databinding.ActivityMainBinding
import com.example.mapkitresultproject.domain.models.SearchState
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.SearchType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.map = binding.mapview.map
        viewModel.setVisibleRegion(viewModel.map.visibleRegion)

        viewModel.setSearchOption(1, SearchType.NONE)
        viewModel.createSession(
            "Минск",
            VisibleRegionUtils.toPolygon(binding.mapview.map.visibleRegion)
        )
        binding.buttonClearRoute.setOnClickListener {
            if(viewModel.getSearchState() is SearchState.Off) {
                viewModel.createSession(
                    "Гродно",
                    VisibleRegionUtils.toPolygon(binding.mapview.map.visibleRegion)
                )
            }
            else Toast.makeText(this,"Loading",Toast.LENGTH_SHORT).show()
        }
        viewModel.getResultedPoint()

        viewModel.getResultedPoint().observe(this){
            Log.d("MyLog", "point: ${it.longitude} ${it.latitude}")
            viewModel.setPointsForRoute(listOf(Point(53.691341, 23.833418),Point(53.689998, 23.837021)))
            viewModel.createSessionCreateRoute()
        }


        viewModel.setDrivingOptions(1)
        viewModel.setVehicleOptions(VehicleType.TRUCK,5000f)
        viewModel.setPointsForRoute(listOf(Point(53.677844, 23.843776),Point(53.688190, 23.824166)))
        viewModel.createSessionCreateRoute()
        viewModel.getResultedRout().observe(this){
            it.forEach {point->
                Log.d("MyLog", "route: ${point.longitude} ${point.latitude}")
            }
        }




//
//        viewModel.map.addInputListener(viewModel.inputListener)
//
//        viewModel.routesCollection = viewModel.map.mapObjects.addCollection()
//        viewModel.placemarksCollection = viewModel.map.mapObjects.addCollection()
//        viewModel.setVisibleRegion(viewModel.map.visibleRegion)
//
//        viewModel.moveToStartLocation()
//
//        viewModel.drivingRouter =
//            DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
//
//        binding.buttonClearRoute.setOnClickListener {
//            viewModel.routePoints = emptyList()
//        }
//        viewModel.submitSearch("Дубко 20")
//        viewModel.submitSearch("Гродненский Зоопарк")
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
}
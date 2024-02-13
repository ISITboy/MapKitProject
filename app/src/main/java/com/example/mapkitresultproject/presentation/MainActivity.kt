package com.example.mapkitresultproject.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.presentation.tabs.TabsFragment
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.StorageManagerViewModel
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //имеется два navController: корневой и для вкладок

    // навигационный контроллер текущего экрана
    private var navController: NavController? = null

    private val topLevelDestinations = setOf(getTabsDestination(), getSignInDestination())

    private val viewModel: StorageManagerViewModel by viewModels()

    // слушатель фрагмента, который отслеживает текущий navController
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController()) // если не вспомогательные navController, то обновляем navController
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        // preparing root nav controller
        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isStartDestination(navController?.currentDestination)) {// если находимя на стортовом экране
            super.onBackPressed()
        } else {
            navController?.popBackStack()
        }
    }

    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())// создается navGraph
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getSignInDestination()
            }
        )
        navController.graph = graph
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)//исбежание утечки памяти
        navController.addOnDestinationChangedListener(destinationListener)// для обновления заголовка в ToolBar
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainers) as NavHostFragment
        return navHost.navController
    }

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, arguments ->
        supportActionBar?.title = prepareTitle(destination.label, arguments)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {

        // code for this method has been copied from Google sources :)

        if (label == null) return ""
        val title = StringBuffer()
        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
        val matcher = fillInPattern.matcher(label)
        while (matcher.find()) {
            val argName = matcher.group(1)
            if (arguments != null && arguments.containsKey(argName)) {
                matcher.appendReplacement(title, "")
                title.append(arguments[argName].toString())
            } else {
                throw IllegalArgumentException(
                    "Could not find $argName in $arguments to fill label $label"
                )
            }
        }
        matcher.appendTail(title)
        return title.toString()
    }

    private fun isSignedIn(): Boolean {
        return false
    }

    //идентификатор главного графа
    private fun getMainNavigationGraphId():Int = R.navigation.main_graph

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getSignInDestination(): Int =R.id.signInFragment
}

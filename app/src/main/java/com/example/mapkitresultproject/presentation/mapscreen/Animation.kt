package com.example.mapkitresultproject.presentation.mapscreen

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.mapkitresultproject.R

class Animation(private val context: Context) {
    private val slideInRouteInfoLayout = AnimationUtils.loadAnimation(context, R.anim.slide_in)
    private val slideOutRouteInfoLayout = AnimationUtils.loadAnimation(context, R.anim.slide_out)

    private val slideInErrorMessageLayout = AnimationUtils.loadAnimation(context,R.anim.slide_in_error_message)
    private val slideOutErrorMessageLayout = AnimationUtils.loadAnimation(context,R.anim.slide_out_error_message)

    val routeInfoAnim = RouteInfoAnim(slideInRouteInfoLayout,slideOutRouteInfoLayout)
    val errorMessageAnim = ErrorMessageAnim(slideInErrorMessageLayout,slideOutErrorMessageLayout)

}
data class RouteInfoAnim(val slideIn:Animation, val slideOut:Animation)
data class ErrorMessageAnim(val slideIn:Animation, val slideOut:Animation)

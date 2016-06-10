package com.david.todo.view.eventlisteners

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import timber.log.Timber

/**
 * Created by DavidHome on 10/06/2016.
 */
class SliderAnimationWrapper(val viewToAnimate: View, val fromX: Float, val toX: Float) : Animation.AnimationListener {
    lateinit var slideOffAnimation: Animation
    val animationDuration: Long = 195

    init {
        slideOffAnimation = TranslateAnimation(fromX,
                                               toX,
                                               viewToAnimate?.translationY!!,
                                               viewToAnimate?.translationY!!)
        slideOffAnimation.interpolator = AccelerateInterpolator()
        slideOffAnimation.duration = animationDuration
        slideOffAnimation.setAnimationListener(this)
    }

    fun start() {
        viewToAnimate.startAnimation(slideOffAnimation)
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
        viewToAnimate.visibility = View.INVISIBLE
        //Fires complete off and fade etc
    }

    override fun onAnimationStart(animation: Animation?) {
    }

}
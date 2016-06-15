package com.david.todo.view.eventlisteners

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

/**
 * Created by DavidHome on 10/06/2016.
 */
class SliderAnimationWrapper(val viewToAnimate: View, val fromX: Float, val toX: Float) : Animation.AnimationListener {
    lateinit var slideOffAnimation: Animation
    val ANIMATION_DURATION: Long = 195
    var afterExitAnimation: FadeAnimation? = null

    constructor(viewToAnimate: View, fromX: Float, toX: Float, afterExitAnimation: FadeAnimation?) : this(viewToAnimate, fromX, toX) {
        this.afterExitAnimation = afterExitAnimation
    }

    init {
        slideOffAnimation = TranslateAnimation(fromX,
                                               toX,
                                               viewToAnimate?.translationY!!,
                                               viewToAnimate?.translationY!!)
        slideOffAnimation.interpolator = AccelerateInterpolator()
        slideOffAnimation.duration = ANIMATION_DURATION
        slideOffAnimation.setAnimationListener(this)
    }

    fun start() {
        viewToAnimate.startAnimation(slideOffAnimation)
    }

    override fun onAnimationRepeat(animation: Animation?) {

    }

    override fun onAnimationEnd(animation: Animation?) {
        viewToAnimate.visibility = View.GONE
        afterExitAnimation?.duration = ANIMATION_DURATION
        afterExitAnimation?.startAnimation()
    }

    override fun onAnimationStart(animation: Animation?) {
    }

}
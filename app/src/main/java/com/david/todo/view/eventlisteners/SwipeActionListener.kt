package com.david.todo.view.eventlisteners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */
class SwipeActionListener() : RecyclerView.OnItemTouchListener {

    val NO_TRANSLATION: Float = 0F
    val SWIPE_OFF_SCALAR: Int = 2

    var selectedViewForeground: ViewGroup? = null

    //Translation item TODO could become a value object
    var deltaMoveX: Float = 0F
    var shouldFlingRightOffScreen = false

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, event: MotionEvent?): Boolean {
        //check if fling on action down
        //if was fling swipe view left or right based on direction, gesture detector can call this
        //class back?

        //if fling detected call back this class

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                findAndSelectViewIfValidViewHolderItem(rv?.findChildViewUnder(event!!.x, event.y), rv, event!!)
            }
            MotionEvent.ACTION_MOVE -> {
                translateViewIfOneSelected(event)
                Timber.d("Delta $deltaMoveX")
                if(event!!.x > -350) { //right direction, better off a percentage /
                    var move = event!!.eventTime - event.downTime
                    Timber.d("Move $move")
                    if(event!!.eventTime - event.downTime < 85) {
                        findAndSelectViewIfValidViewHolderItem(rv?.findChildViewUnder(event!!.x, event.y), rv, event!!)
                        if(selectedViewForeground == null) return false
                        SliderAnimationWrapper(selectedViewForeground!!,
                                selectedViewForeground?.translationX!!,
                                selectedViewForeground?.width!!.toFloat())
                                .start()
                        selectedViewForeground = null
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if(shouldFlingRightOffScreen) {
                    if(selectedViewForeground != null) {
                        SliderAnimationWrapper(selectedViewForeground!!,
                                               selectedViewForeground?.translationX!!,
                                               selectedViewForeground?.width!!.toFloat())
                                               .start()
                    }
                }

                deSelectViewHolder()
            }
        }

        return false
    }

    fun swipeRight() {
        Timber.d("RIGHT")
//        SliderAnimationWrapper(selectedViewForeground!!,
//                selectedViewForeground?.translationX!!,
//                selectedViewForeground?.width!!.toFloat())
//                .start()
    }

    /**
     * Finds out if view is part of this rv's list and a valid item type, as only Pending
     * items are allowed to be the swiped
     */
    fun findAndSelectViewIfValidViewHolderItem(view: View?, recyclerView: RecyclerView?, motionEvent: MotionEvent) {
        val view = view?.let { it } ?: return

        if(selectedViewForeground == null) { //First time at selecting a view?
            var viewHolder = recyclerView?.getChildViewHolder(view) //get rv's view holder
            if(viewHolder is PendingItemViewHolder) { //are we the right type of viewholder? TODO (As a lib parent view could be of certain type
                var pendingItemHolder = viewHolder
                selectViewHolder(pendingItemHolder.taskForeground, motionEvent)
//                Timber.d("View selected, translation ${selectedViewForeground?.translationX}")
            } else {
                selectViewHolder(null, motionEvent)
            }
        }
    }

    /**
     * Selects viewholder, records it as a local variable as well as associates a delta value with it
     */
    fun selectViewHolder(view: ViewGroup?, motionEvent: MotionEvent) {
        selectedViewForeground = view
        if(view != null) {
            deltaMoveX = selectedViewForeground!!.x - motionEvent.rawX
        }

    }

    fun deSelectViewHolder() {
        if(selectedViewForeground != null) {
            selectedViewForeground?.translationX = NO_TRANSLATION
            deltaMoveX = NO_TRANSLATION
        }
        selectedViewForeground = null
    }

    fun translateViewIfOneSelected(event: MotionEvent?) {
        if(selectedViewForeground != null) {
            val moveX = deltaMoveX + event?.rawX!!
            selectedViewForeground?.translationX = moveX
            if(moveX.toInt() > selectedViewForeground!!.width / SWIPE_OFF_SCALAR) {
                shouldFlingRightOffScreen = true
            } else {
                shouldFlingRightOffScreen = false
            }
        }
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        Timber.d("onRequestDisallowInterceptTouchEvent")
    }

}
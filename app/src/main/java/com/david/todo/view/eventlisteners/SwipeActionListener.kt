package com.david.todo.view.eventlisteners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.*
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.HolderType
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */

/**
 * @param context - Used for getting view configuration in determining fling distance per device
 * @param checkListAdapter - Used for calling specific methods on the recycler adapter for handling removals
 */
class SwipeActionListener(val context: Context, val checkListAdapter: ChecklistAdapter) : RecyclerView.OnItemTouchListener {

    val NO_TRANSLATION: Float = 0F
    val SWIPE_OFF_SCALAR: Int = 2
    val FLING_THRESHOLD: Float = 1200F //"dropping" back will result in a false fling

    //TODO turn into value object
    var selectedViewForeground: ViewGroup? = null
    var selectedViewHolder: PendingItemViewHolder? = null

    var shouldFlingRightOffScreen: Boolean = false

    //Translation item TODO could become a value object
    var deltaMoveX: Float = 0F
    var velocityTracker : VelocityTracker? = null

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, event: MotionEvent?): Boolean {

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                findAndSelectViewIfValidViewHolderItem(rv?.findChildViewUnder(event!!.x, event.y), rv, event!!)
                if(selectedViewForeground != null) {
                    velocityTracker = VelocityTracker.obtain()
                    velocityTracker?.addMovement(event)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                translateViewIfOneSelected(event)
                if(selectedViewForeground != null) {
                    velocityTracker?.addMovement(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                detectIfFlingOnValidItem(event)
                if(shouldFlingRightOffScreen && selectedViewHolder != null) {
                    if(selectedViewForeground != null) {
                        var fadeAnimation = FadeAnimation(1F, 0F, selectedViewHolder!!, checkListAdapter)
                        SliderAnimationWrapper(selectedViewForeground!!,
                                               selectedViewForeground?.translationX!!,
                                               selectedViewForeground?.width!!.toFloat(),
                                               fadeAnimation)
                                               .start()
                    }
                }

                deSelectViewHolder()
            }
        }

        return false
    }

    fun detectIfFlingOnValidItem(event: MotionEvent?) {
        if(selectedViewForeground != null) {
            var viewConfiguration = ViewConfiguration.get(context)
            velocityTracker?.addMovement(event)
            velocityTracker?.computeCurrentVelocity(1000, viewConfiguration.scaledMaximumFlingVelocity.toFloat())
            var velocity = velocityTracker?.getXVelocity(event!!.getPointerId(event!!.actionIndex))

            //TODO will need to determine direction also when looking for delete
            if(Math.abs(velocity!!.toInt()) > FLING_THRESHOLD) {
                shouldFlingRightOffScreen = true
            }

            velocityTracker?.recycle()
        }
    }

    /**
     * Finds out if view is part of this rv's list and a valid item type, as only Pending
     * items are allowed to be the swiped
     */
    fun findAndSelectViewIfValidViewHolderItem(view: View?, recyclerView: RecyclerView?, motionEvent: MotionEvent) {
        val view = view?.let { it } ?: return

        if(selectedViewForeground == null) {
            var viewHolder = recyclerView?.getChildViewHolder(view) //get rv's view holder
            if(viewHolder is PendingItemViewHolder) { //are we the right type of viewholder? TODO (As a lib parent view could be of certain type
                var pendingItemHolder = viewHolder
                selectViewHolder(pendingItemHolder, pendingItemHolder.taskForeground, motionEvent)
//                Timber.d("View selected, translation ${selectedViewForeground?.translationX}")
            } else {
                selectViewHolder(null, null, motionEvent)
            }
        }
    }

    /**
     * Selects viewholder, records it as a local variable as well as associates a delta value with it
     */
    fun selectViewHolder(pendingItemViewHolder: PendingItemViewHolder?, view: ViewGroup?, motionEvent: MotionEvent) {
        selectedViewForeground = view
        selectedViewHolder = pendingItemViewHolder
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
        selectedViewHolder = null
    }

    fun translateViewIfOneSelected(event: MotionEvent?) {
        if(selectedViewForeground != null) {
            //how to tell if moving right
            var moveX = deltaMoveX + event?.rawX!!

            if(moveX < 0) {
                selectedViewHolder?.actionSwitch?.displayedChild = PendingItemViewHolder.COMPLETE_VIEW
            } else {
                selectedViewHolder?.actionSwitch?.displayedChild = PendingItemViewHolder.DELETE_VIEW
            }

            if(moveX < -200F) {
                moveX = -200F
                //change vh state to pending delete
                //on render stays that way, (also checked on mouse up)
                selectedViewHolder?.viewType = HolderType.DELETE_TOGGLE
            } else {
                selectedViewHolder?.viewType = HolderType.PENDING
            }

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
package com.david.todo.view.eventlisteners

import android.view.GestureDetector
import android.view.MotionEvent
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */
class SwipeActionListener(val viewHolder: PendingItemViewHolder?) : GestureDetector.SimpleOnGestureListener() {
    val SWIPE_THRESHOLD = 100 //TODO inject in
    val SWIPE_VELOCITY_THRESHOLD = 100

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        var handled = false;

        var diffX = e2?.x!!.minus(e1!!.x);
        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffX > 0) {
                onSwipeRight();
            } else {
                onSwipeLeft();
            }
            handled = true;
        }

        return handled
    }

    fun onSwipeRight() {
        Timber.d("onSwipeRight")
    }

    fun onSwipeLeft() {
        Timber.d("onSwipeLeft")
    }
}
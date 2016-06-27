package com.david.todo.view.eventlisteners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.david.todo.adapter.ChecklistAdapter
import timber.log.Timber

/**
 * Created by DavidHome on 07/06/2016.
 */

/**
 * @param context - Used for getting view configuration in determining fling distance per device
 * @param checkListAdapter - Used for calling specific methods on the recycler adapter for handling removals
 */
class SwipeActionListener(val context: Context, val checkListAdapter: ChecklistAdapter) : RecyclerView.OnItemTouchListener {

    companion object {
        val DELETE_TOGGLE_TRANSLATE_X = -200F
        val PENDING_TRANSLATE_X = 0F
    }

    lateinit var interactionHandler: InteractionHandler

    init {
        interactionHandler = InteractionHandler(checkListAdapter, context)
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
        Timber.d("onTouchEvent")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, event: MotionEvent?): Boolean {

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                interactionHandler.beginInteractionWith(event = event,
                                                        recyclerView = rv)
            }
            MotionEvent.ACTION_MOVE -> {
                interactionHandler.translateSelectedViewWith(event)
            }
            MotionEvent.ACTION_UP -> {
                interactionHandler.finaliseInteractionWith(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                Timber.d("ITEM - CANCEL")
            }
        }

        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        Timber.d("onRequestDisallowInterceptTouchEvent")
    }

}
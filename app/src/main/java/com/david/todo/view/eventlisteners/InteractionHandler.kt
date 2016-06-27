package com.david.todo.view.eventlisteners

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.HolderType
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import com.david.todo.model.PendingCheckItemModel

/**
 * Created by DavidHome on 21/06/2016.
 */
class InteractionHandler(val checkListAdapter: ChecklistAdapter,
                         val context: Context) {

    companion object {
        val DELETE_TOGGLE_TRANSLATE_X = -200F
    }

    val FLING_THRESHOLD: Float = 1200F
    val SWIPE_OFF_SCALAR: Int = 2

    var selectedInteractionItem: InteractionItem? = null
    var velocityTracker: VelocityTracker? = null

    fun beginInteractionWith(event: MotionEvent?, recyclerView: RecyclerView?) {
        val view = recyclerView?.findChildViewUnder(event!!.x, event.y) ?: return

        if(selectedInteractionItem == null) {
            var viewHolder = recyclerView?.getChildViewHolder(view)
            if(viewHolder is PendingItemViewHolder) {
                velocityTracker = VelocityTracker.obtain();
                velocityTracker?.addMovement(event)

                selectedInteractionItem = InteractionItem(viewHolder.taskForeground,
                                                          viewHolder.actionSwitch,
                                                          viewHolder,
                                                          viewHolder.taskForeground.x - event!!.rawX,
                                                          viewHolder.viewType,
                                                          event.x)
            } else {
                selectedInteractionItem = null
            }
        }
    }

    fun translateSelectedViewWith(event: MotionEvent?) {
        if(selectedInteractionItem == null) return
        //calculate move
        var moveX = selectedInteractionItem!!.foregroundViewPosition + event?.rawX!!

        //if currently in delete state, can only go to 0, cannot go more than Configured position (on mouse up)
        if(selectedInteractionItem!!.actionViewType == HolderType.DELETE_TOGGLE) {
            if(moveX >= 0) {
                moveX = 0F
            } else if (moveX <= DELETE_TOGGLE_TRANSLATE_X) {
                moveX = DELETE_TOGGLE_TRANSLATE_X
            }
        } else if(selectedInteractionItem!!.actionViewType == HolderType.PENDING) {
            if(moveX < DELETE_TOGGLE_TRANSLATE_X) {
                moveX = DELETE_TOGGLE_TRANSLATE_X
            }
        }

        if(moveX > 0) {
            selectedInteractionItem!!.background.displayedChild = 0
        } else {
            selectedInteractionItem!!.background.displayedChild = 1
        }

        selectedInteractionItem!!.foreground.translationX = moveX
        velocityTracker?.addMovement(event)
    }

    fun finaliseInteractionWith(event: MotionEvent?) {
        if(selectedInteractionItem == null) return
        var finalPosition = selectedInteractionItem!!.foreground.translationX

        if(selectedInteractionItem?.actionViewType == HolderType.PENDING) {
            if(finalPosition < DELETE_TOGGLE_TRANSLATE_X / SWIPE_OFF_SCALAR) {
                selectedInteractionItem!!.foreground.translationX = DELETE_TOGGLE_TRANSLATE_X
                selectedInteractionItem!!.actionViewType = HolderType.DELETE_TOGGLE
                updateRecyclerModelDeleteState(true) //to render it as deleted next pass
            } else if(finalPosition > selectedInteractionItem!!.foreground.width / 2 || flingedToRight(event)) {
                slideOffScreenToCompleted() //slide away
                selectedInteractionItem!!.actionViewType = HolderType.COMPLETED
                updateRecyclerModelDeleteState(false)
            } else {
                selectedInteractionItem!!.foreground.translationX = 0F
                updateRecyclerModelDeleteState(false)
            }

        } else if (selectedInteractionItem?.actionViewType == HolderType.DELETE_TOGGLE) {
            if(finalPosition >= DELETE_TOGGLE_TRANSLATE_X / SWIPE_OFF_SCALAR) {
                selectedInteractionItem!!.foreground.translationX = 0F
                selectedInteractionItem!!.actionViewType = HolderType.PENDING
                updateRecyclerModelDeleteState(false)
            } else {
                selectedInteractionItem!!.foreground.translationX = DELETE_TOGGLE_TRANSLATE_X
                selectedInteractionItem!!.actionViewType = HolderType.DELETE_TOGGLE
                updateRecyclerModelDeleteState(true) //to render it as deleted next pass
            }
        }

        velocityTracker?.recycle()
        selectedInteractionItem = null
    }

    private fun flingedToRight(event: MotionEvent?) : Boolean {

        var viewConfiguration = ViewConfiguration.get(context)
        velocityTracker?.addMovement(event)
        velocityTracker?.computeCurrentVelocity(1000, viewConfiguration.scaledMaximumFlingVelocity.toFloat())

        var velocity = velocityTracker?.getXVelocity(event!!.getPointerId(event!!.actionIndex))

        if(Math.abs(velocity!!.toInt()) > FLING_THRESHOLD) {
            if(selectedInteractionItem!!.interactionStartPosition < event!!.x) {
                return true
            }
        }

        return false
    }

    private fun slideOffScreenToCompleted() {
        var fadeAnimation = FadeAnimation(1F, 0F, selectedInteractionItem!!.pendingItemViewHolder, checkListAdapter)
        SliderAnimationWrapper(selectedInteractionItem!!.foreground,
                selectedInteractionItem!!.foreground.translationX,
                selectedInteractionItem!!.foreground.width.toFloat(),
                fadeAnimation)
                .start()
    }

    private fun updateRecyclerModelDeleteState(deleteToggled: Boolean) {
        if(selectedInteractionItem == null) return
        var adapterPosition = selectedInteractionItem!!.pendingItemViewHolder.adapterPosition
        if(adapterPosition == RecyclerView.NO_POSITION) return
        var pendingItem = checkListAdapter.itemList[adapterPosition] as PendingCheckItemModel
        pendingItem.isDeleteToggled = deleteToggled
    }
}
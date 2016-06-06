package com.david.todo.view.eventlisteners

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.AbsListView
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.CompletedItemViewHolder
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 03/04/2016.
 */
class TouchEventHelper(val checkListAdapter: ChecklistAdapter,
                       val maxLeftSwipePx: Int) : ItemTouchHelper.Callback() {

    val animationDuration: Long = 700

    /*below values are provided by the AndroidSDK onSwiped callback direction, this is not currently documented,
     values below make for more readable code*/
    val LEFT: Int = 16
    val RIGHT: Int = 32

    override fun isLongPressDragEnabled() : Boolean {
        return true;
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        if(viewHolder is CompletedItemViewHolder) return true
        checkListAdapter.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition);
        return true;
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
//        if(viewHolder is PendingItemViewHolder == false) return
//        var pendingItem = viewHolder as PendingItemViewHolder
//        Timber.d("Direction: $direction")
//        if(direction == LEFT) {
//
//        } else if(direction == RIGHT && pendingItem.actionSwitch.currentView.id == R.id.complete_task_background) { //input and for an is showing on delete/toggled state
//            val scaleAnimation = FadeAnimation(1F, 0F, viewHolder, checkListAdapter)
//            scaleAnimation.duration = animationDuration
//            pendingItem.itemView.startAnimation(scaleAnimation);
//        }
    }

    val SWIPE_LEFT_THRESHOLD: Float = 300F
    val COMPLETE_VIEW: Int = 0
    val DELETE_VIEW: Int = 1

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if(viewHolder is CompletedItemViewHolder) return
        if (viewHolder?.adapterPosition == -1) {
            return;
        }

        val holder = viewHolder as PendingItemViewHolder

        if(actionState.equals(ItemTouchHelper.ACTION_STATE_DRAG)) {
            if(holder.topBorder.visibility == View.INVISIBLE) {
                holder.topBorder.visibility = View.VISIBLE
            }
        }

        if(isCurrentlyActive == false) {
            if(holder.topBorder.visibility == View.VISIBLE) {
                holder.topBorder.visibility = View.INVISIBLE
            }
        }

        var drawX = dX
        if(actionState.equals(ItemTouchHelper.ACTION_STATE_SWIPE)) {
            Timber.d("DrawX $drawX") //TODO drawX is + or - from the position drawn

            if(drawX < 0) { //LEFT
                //Also check here we're not x into the complete translation
                //calculate delta
                holder.actionSwitch.displayedChild = DELETE_VIEW
                if(hasHitLeftSwipeThreshold(holder.taskForeground.translationX) == false) {
                    holder.taskForeground.translationX = drawX
                }

                Timber.d("Translation ${holder.taskForeground.translationX}")
            } else { //RIGHT
                if(holder.actionSwitch.displayedChild == DELETE_VIEW) {
                    if((holder.taskForeground.translationX < 0) == false) { // Ok to show complete when above 0 translation
                        holder.actionSwitch.displayedChild = COMPLETE_VIEW
                    }
                }

                holder.taskForeground.translationX = drawX
            }

            drawX = 0F //Keeps background view still
//            Timber.d("draw animate ${viewHolder?.adapterPosition}")
        }

        super.onChildDraw(c, recyclerView, viewHolder, drawX, dY, actionState, isCurrentlyActive)
    }

    fun hasHitLeftSwipeThreshold(translationX: Float) : Boolean {
        if(Math.abs(translationX) > SWIPE_LEFT_THRESHOLD) { return true } else { return false }
    }
}

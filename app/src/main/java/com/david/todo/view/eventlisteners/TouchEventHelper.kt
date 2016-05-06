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
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.ItemViewHolder
import timber.log.Timber

/**
 * Created by DavidHome on 03/04/2016.
 */
class TouchEventHelper(val checkListAdapter: ChecklistAdapter) : ItemTouchHelper.Callback() {

    val animationDuration: Long = 700

    override fun isLongPressDragEnabled() : Boolean {
        return true;
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        checkListAdapter.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition);
        return true;
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        var parentContainer = viewHolder as ItemViewHolder;
        val scaleAnimation = FadeAnimation(1F, 0F, viewHolder, checkListAdapter)
        scaleAnimation.duration = animationDuration
        parentContainer.itemView.startAnimation(scaleAnimation);
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder?.adapterPosition == -1) {
            return;
        }

        var drawX = dX
        if(actionState.equals(ItemTouchHelper.ACTION_STATE_SWIPE)) {
            //TODO make this call composed so is non specific to a concrete viewholder
            val holder = viewHolder as ItemViewHolder
            holder.taskForeground.translationX = dX

            drawX = 0F //Keeps background view still
//            Timber.d("draw animate ${viewHolder?.adapterPosition}")
        }

        super.onChildDraw(c, recyclerView, viewHolder, drawX, dY, actionState, isCurrentlyActive)
    }
}

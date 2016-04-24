package com.david.todo.view.eventlisteners

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import com.david.todo.adapter.ChecklistAdapter
import timber.log.Timber

/**
 * Created by DavidHome on 03/04/2016.
 */
class TouchEventHelper(val checkListAdapter: ChecklistAdapter) : ItemTouchHelper.Callback() {
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
        var parentContainer = viewHolder as ChecklistAdapter.ItemViewHolder;
        val fadeAnimation = FadeAnimation(1F, 0F, viewHolder, checkListAdapter)
        fadeAnimation.duration = 700
        parentContainer.itemView.animation = fadeAnimation
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (viewHolder?.adapterPosition == -1) {
            return;
        }

        var drawX = dX
        if(actionState.equals(ItemTouchHelper.ACTION_STATE_SWIPE)) {
            //TODO make this call composed so is non specific to a concrete viewholder
            val holder = viewHolder as ChecklistAdapter.ItemViewHolder
            val layoutParams = holder.taskForeground.layoutParams as FrameLayout.LayoutParams
            layoutParams.leftMargin = dX.toInt()
            holder.taskForeground.layoutParams = layoutParams
            drawX = 0F
            Timber.d("draw animate ${viewHolder?.adapterPosition}")
        }

        super.onChildDraw(c, recyclerView, viewHolder, drawX, dY, actionState, isCurrentlyActive)
    }
}

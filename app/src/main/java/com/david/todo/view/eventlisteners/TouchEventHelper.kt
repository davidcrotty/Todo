package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.david.todo.adapter.ChecklistAdapter

/**
 * Created by DavidHome on 03/04/2016.
 */
class TouchEventHelper(val checkListAdapter: ChecklistAdapter) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled() : Boolean {
        return true;
    }

    override fun isItemViewSwipeEnabled() : Boolean {
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
        checkListAdapter.onItemDismiss(viewHolder!!.adapterPosition);
    }

}

package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.david.todo.adapter.ChecklistAdapter

/**
 * Created by DavidHome on 03/07/2016.
 */
class ListDragHandler(val checkListAdapter: ChecklistAdapter) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        var dragFlags = ItemTouchHelper.UP.or(ItemTouchHelper.DOWN)
        return makeMovementFlags(dragFlags, dragFlags)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        checkListAdapter.onItemMove(viewHolder!!.adapterPosition, target!!.adapterPosition)
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }

}
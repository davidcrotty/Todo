package com.david.todo.presenter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.david.todo.R
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import com.david.todo.model.CheckItem
import com.david.todo.model.PendingToCompleteItemHolder
import com.david.todo.model.CompletedCheckItemModel
import com.david.todo.model.PendingCheckItemModel
import com.david.todo.view.activity.TaskListActivity
import java.util.*

/**
 * Created by DavidHome on 19/04/2016.
 */
class TaskListPresenter(val taskListActivity: TaskListActivity) {
    /**
     * Stores completed item so it can be identified and undone.
     */
    fun storeAndDisplaySnackBarFor(pendingCheckItemModel: PendingCheckItemModel,
                                   completedCheckItemModel: CompletedCheckItemModel,
                                   position: Int) {
        taskListActivity.storeIntentFor(PendingToCompleteItemHolder(pendingCheckItemModel, completedCheckItemModel, position))
        taskListActivity.showSnackbarWith(taskListActivity.resources.getString(R.string.checklist_completed_text),
                                         taskListActivity.resources.getColor(R.color.green),
                                         View.OnClickListener {
                                             taskListActivity.undoCompletedItem()
                                         })
    }

    /**
     * @param task text to add to the adapter as a pending item with
     * @param viewContext the context of the view for the input manager to use as a binder token for hide/showing the keyboard
     */
    fun addPendingItemToAdapterWith(task: String, viewContext: View) {
        val lastPosition = taskListActivity.getLastCompletedItemPosition()
        val pendingItem = PendingCheckItemModel(task)
        taskListActivity.addPendingItemToAdapterWith(pendingItem, lastPosition)
        taskListActivity.dismissKeyboardWith(viewContext)
        taskListActivity.showDeleteToggleIcon()
    }

    fun removePendingItemFromAdapterWith(pendingItemViewHolder: PendingItemViewHolder) {
        val position = pendingItemViewHolder.adapterPosition
        if(position == RecyclerView.NO_POSITION) return
        taskListActivity.pendingItemDeleted(position)
        //if adapter has any pending items
        var remainingItems = taskListActivity.checkListAdapter.itemList
        var hasAPendingItem = false
        for(checkItem in remainingItems) {
            if(checkItem is PendingCheckItemModel) {
                hasAPendingItem = true
                break
            }
        }

        if(hasAPendingItem) return
        taskListActivity.hideDeleteToggleIcon()
    }

    fun loadTaskItems(checkItems: ArrayList<CheckItem>?) {
//        var itemList = checkItems ?: arrayListOf(PendingCheckItemModel("Prepare meeting room"),
//                PendingCheckItemModel("Meet cat smugglers"),
//                PendingCheckItemModel("Test AV equipment"),
//                PendingCheckItemModel("Review project proposal"),
//                PendingCheckItemModel("Update statement of work"),
//                CompletedCheckItemModel("Update statement of work2"))


        var checkItems = checkItems?: ArrayList<CheckItem>()
        if(checkItems.isEmpty()) taskListActivity.delegateHideDropShadow()
        taskListActivity.initAdapterWith(checkItems)
        taskListActivity.setScrollBehaviourWith()
    }
}
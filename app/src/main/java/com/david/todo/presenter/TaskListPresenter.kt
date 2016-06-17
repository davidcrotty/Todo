package com.david.todo.presenter

import com.david.todo.model.CheckItem
import com.david.todo.model.CheckItemHolder
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
        taskListActivity.storeIntentFor(CheckItemHolder(pendingCheckItemModel, completedCheckItemModel, position))
        taskListActivity.showSnackbar()
    }

    fun addPendingItemToAdapterWith(task: String) {
        val lastPosition = taskListActivity.getLastCompletedItemPosition()
        val pendingItem = PendingCheckItemModel(task)
        taskListActivity.addPendingItemToAdapterWith(pendingItem, lastPosition)
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
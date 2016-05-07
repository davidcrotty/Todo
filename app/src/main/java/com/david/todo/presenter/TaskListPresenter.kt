package com.david.todo.presenter

import com.david.todo.model.CheckItemHolder
import com.david.todo.model.PendingItemModel
import com.david.todo.view.activity.TaskListActivity

/**
 * Created by DavidHome on 19/04/2016.
 */
class TaskListPresenter(val taskListActivity: TaskListActivity) {
    fun storeAndDisplaySnackBarFor(pendingItemModel: PendingItemModel,
                                   position: Int) {
        taskListActivity.storeIntentFor(CheckItemHolder(pendingItemModel, position))
        taskListActivity.showSnackbar()
    }
}
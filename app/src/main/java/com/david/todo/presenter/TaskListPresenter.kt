package com.david.todo.presenter

import com.david.todo.model.CheckItemHolder
import com.david.todo.model.PendingCheckItemModel
import com.david.todo.view.activity.TaskListActivity

/**
 * Created by DavidHome on 19/04/2016.
 */
class TaskListPresenter(val taskListActivity: TaskListActivity) {
    fun storeAndDisplaySnackBarFor(pendingCheckItemModel: PendingCheckItemModel,
                                   position: Int) {
        taskListActivity.storeIntentFor(CheckItemHolder(pendingCheckItemModel, position))
        taskListActivity.showSnackbar()
    }
}
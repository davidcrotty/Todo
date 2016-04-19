package com.david.todo.presenter

import com.david.todo.model.CheckItemHolder
import com.david.todo.model.CheckItemModel
import com.david.todo.view.activity.TaskListActivity

/**
 * Created by DavidHome on 19/04/2016.
 */
class TaskListPresenter(val taskListActivity: TaskListActivity) {
    fun storeAndDisplaySnackBarFor(checkItemModel: CheckItemModel,
                                   position: Int) {
        taskListActivity.storeIntentFor(CheckItemHolder(checkItemModel, position))
        taskListActivity.showSnackbar()
    }
}
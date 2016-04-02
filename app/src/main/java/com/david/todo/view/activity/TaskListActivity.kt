package com.david.todo.view.activity

import android.os.Bundle
import com.david.todo.R
import com.david.todo.view.BaseActivity

/**
 * Created by DavidHome on 02/04/2016.
 */
class TaskListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
    }
}
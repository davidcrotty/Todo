package com.david.todo.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.ListView
import butterknife.Bind
import butterknife.ButterKnife
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.view.BaseActivity
import com.david.todo.view.eventlisteners.TouchEventHelper

/**
 * Created by DavidHome on 02/04/2016.
 */
class TaskListActivity : BaseActivity() {

    lateinit var _checkList: RecyclerView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        _checkList = findViewById(R.id.check_list) as RecyclerView
        init()
    }

    private fun init() {
        val itemList = arrayListOf("one", "two", "three", "four", "five")
        val adapter = ChecklistAdapter(itemList)
        _checkList.setHasFixedSize(true)
        _checkList.adapter = adapter
        _checkList.layoutManager = LinearLayoutManager(this)

        val touchEventHelper = TouchEventHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(touchEventHelper)
        itemTouchHelper.attachToRecyclerView(_checkList)
    }
}
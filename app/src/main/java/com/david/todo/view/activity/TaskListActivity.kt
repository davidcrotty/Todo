package com.david.todo.view.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.ListView
import butterknife.Bind
import butterknife.ButterKnife
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.model.CheckItemModel
import com.david.todo.view.BaseActivity
import com.david.todo.view.decorators.DividerItemDecoration
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
        val resources = resources
        val itemList = arrayListOf(CheckItemModel("Prepare meeting room", resources.getColor(R.color.purple)),
                                   CheckItemModel("Review project proposal", resources.getColor(R.color.orange)),
                                   CheckItemModel("Contact catering team", resources.getColor(R.color.light_blue)),
                                   CheckItemModel("Email team reminder", resources.getColor(R.color.red)),
                                   CheckItemModel("Test AV equipment", resources.getColor(R.color.teal)));
        val adapter = ChecklistAdapter(itemList)
        _checkList.setHasFixedSize(true)
        _checkList.adapter = adapter
        _checkList.layoutManager = LinearLayoutManager(this)
//        _checkList.addItemDecoration(DividerItemDecoration(this, R.drawable.list_divider))

        val touchEventHelper = TouchEventHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(touchEventHelper)
        itemTouchHelper.attachToRecyclerView(_checkList)
    }
}
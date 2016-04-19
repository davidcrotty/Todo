package com.david.todo.view.activity

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import butterknife.bindView
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.model.CheckItemHolder
import com.david.todo.model.CheckItemModel
import com.david.todo.presenter.TaskListPresenter
import com.david.todo.view.BaseActivity
import com.david.todo.view.eventlisteners.IHandleListener
import com.david.todo.view.eventlisteners.TouchEventHelper

/**
 * Created by DavidHome on 02/04/2016.
 */
class TaskListActivity : BaseActivity(), IHandleListener {
    val rootView: CoordinatorLayout by bindView(R.id.root_view)
    val MOST_RECENTLY_REMOVED_MODEL: String = "MOST_RECENTLY_REMOVED_MODEL"

    lateinit var _List_presenter: TaskListPresenter
    lateinit var _checkList: RecyclerView
    lateinit var _itemTouchHelper: ItemTouchHelper
    lateinit var _checkListAdapter: ChecklistAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        _List_presenter = TaskListPresenter(this)
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
        _checkListAdapter = ChecklistAdapter(itemList, _List_presenter, this, this)
        _checkList.setHasFixedSize(true)
        _checkList.adapter = _checkListAdapter
        _checkList.layoutManager = LinearLayoutManager(this)
        var touchEventHelper = TouchEventHelper(_checkListAdapter)

        _itemTouchHelper = ItemTouchHelper(touchEventHelper)
        _itemTouchHelper.attachToRecyclerView(_checkList)
    }

    override fun onHandleDown(viewHolder: RecyclerView.ViewHolder) {
        _itemTouchHelper.startDrag(viewHolder)
    }

    fun showSnackbar() {
        Snackbar.make(rootView,
                      resources.getString(R.string.checklist_text),
                      Snackbar.LENGTH_LONG)
                          .setAction(resources.getString(R.string.checklist_action), {
                              val checkItemHolder = intent.getSerializableExtra(MOST_RECENTLY_REMOVED_MODEL) as CheckItemHolder
                              _checkListAdapter.restoreItemWith(checkItemHolder?.position, checkItemHolder?.checkItemModel)
                          })
                      .setActionTextColor(resources.getColor(R.color.green))
                      .show()
    }

    fun storeIntentFor(taskItemHolder: CheckItemHolder) {
        intent.putExtra(MOST_RECENTLY_REMOVED_MODEL, taskItemHolder)
    }
}
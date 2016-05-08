package com.david.todo.view.activity

import android.content.BroadcastReceiver
import android.content.res.TypedArray
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.Menu
import android.widget.LinearLayout
import butterknife.bindView
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.model.CheckItem
import com.david.todo.model.CheckItemHolder
import com.david.todo.model.CompletedCheckItemModel
import com.david.todo.model.PendingCheckItemModel
import com.david.todo.presenter.TaskListPresenter
import com.david.todo.view.BaseActivity
import com.david.todo.view.eventlisteners.IHandleListener
import com.david.todo.view.eventlisteners.TouchEventHelper
import timber.log.Timber
import java.util.*

/**
 * Created by DavidHome on 02/04/2016.
 */
class TaskListActivity : BaseActivity(), IHandleListener {
    val rootView: CoordinatorLayout by bindView(R.id.root_view)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val MOST_RECENTLY_REMOVED_MODEL: String = "MOST_RECENTLY_REMOVED_MODEL"
    val CHECK_ITEM_LIST: String = "CHECK_ITEM_LIST"


    lateinit var _List_presenter: TaskListPresenter
    lateinit var _checkListView: RecyclerView
    lateinit var _itemTouchHelper: ItemTouchHelper
    lateinit var _checkListAdapter: ChecklistAdapter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(toolbar)
        _List_presenter = TaskListPresenter(this)
        _checkListView = findViewById(R.id.check_list) as RecyclerView
        initAdapter()
    }

    private fun initAdapter() {
        var itemList =
        if(intent.hasExtra(CHECK_ITEM_LIST)) {
           intent.getSerializableExtra(CHECK_ITEM_LIST)
        } else {
            arrayListOf(PendingCheckItemModel("Prepare meeting room"),
                    PendingCheckItemModel("Meet cat smugglers"),
                    PendingCheckItemModel("Test AV equipment"),
                    PendingCheckItemModel("Review project proposal"),
                    CompletedCheckItemModel("Update statement of work"));
        }

        _checkListAdapter = ChecklistAdapter(itemList as ArrayList<CheckItem>, _List_presenter, this, this)
        _checkListView.setHasFixedSize(true)
        _checkListView.adapter = _checkListAdapter
        _checkListView.layoutManager = LinearLayoutManager(this)

        var touchEventHelper = TouchEventHelper(_checkListAdapter)
        _itemTouchHelper = ItemTouchHelper(touchEventHelper)
        _itemTouchHelper.attachToRecyclerView(_checkListView)
    }

    override fun onPause() {
        super.onPause()
        intent.putExtra(CHECK_ITEM_LIST, _checkListAdapter.itemList)
    }

    override fun onHandleDown(viewHolder: RecyclerView.ViewHolder) {
        _itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        val toggleCurrentItems = menu.findItem(R.id.toggle_completed_items);
        toggleCurrentItems.setOnMenuItemClickListener {
            item -> if(item.isChecked) {
                item.isChecked = false
            } else {
                item.isChecked = true
            }
            true
        }
        return true
    }

    fun showSnackbar() {
        Snackbar.make(rootView,
                      resources.getString(R.string.checklist_text),
                      Snackbar.LENGTH_LONG)
                          .setAction(resources.getString(R.string.checklist_action), {
                              val checkItemHolder = intent.getSerializableExtra(MOST_RECENTLY_REMOVED_MODEL) as CheckItemHolder
                              _checkListAdapter.restoreItemWith(checkItemHolder?.position,
                                                                checkItemHolder?.pendingCheckItemModel,
                                                                checkItemHolder.completedCheckItemModel)
                          })
                      .setActionTextColor(resources.getColor(R.color.green))
                      .setCallback(object: Snackbar.Callback() {
                            override fun onDismissed(snackbar: Snackbar, event: Int) {
                                Timber.d("DISMISSED")
                                _checkListAdapter.allowViewholderTypeTransform(true)
                            }

                            override  fun onShown(snackbar: Snackbar) {
                                Timber.d("SHOWN")
                                _checkListAdapter.allowViewholderTypeTransform(false)
                              }
                      })
                      .show()
    }

    fun storeIntentFor(taskItemHolder: CheckItemHolder) {
        intent.putExtra(MOST_RECENTLY_REMOVED_MODEL, taskItemHolder)
    }
}
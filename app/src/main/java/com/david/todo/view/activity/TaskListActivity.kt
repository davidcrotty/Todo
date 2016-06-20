package com.david.todo.view.activity

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.RelativeLayout
import butterknife.bindView
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.HolderType
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import com.david.todo.model.CheckItem
import com.david.todo.model.CheckItemHolder
import com.david.todo.model.PendingCheckItemModel
import com.david.todo.presenter.TaskListPresenter
import com.david.todo.view.BaseActivity
import com.david.todo.view.eventlisteners.SwipeActionListener
import com.david.todo.view.widgets.EnterItemView
import timber.log.Timber
import java.util.*

/**
 * Created by DavidHome on 02/04/2016.
 */
class TaskListActivity : BaseActivity() {
    val rootView: CoordinatorLayout by bindView(R.id.root_view)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val enterItemWidget: EnterItemView by bindView(R.id.enter_item_view)
    val checkListView: RecyclerView by bindView(R.id.check_list)
    val deleteToggleIcon: RelativeLayout by bindView(R.id.delete_action_icon)

    val MOST_RECENTLY_REMOVED_MODEL: String = "MOST_RECENTLY_REMOVED_MODEL"
    val CHECK_ITEM_LIST: String = "CHECK_ITEM_LIST"

    lateinit var swipeActionListener: SwipeActionListener
    lateinit var listPresenter: TaskListPresenter
    lateinit var checkListAdapter: ChecklistAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(toolbar)

        listPresenter = TaskListPresenter(this)
        enterItemWidget.attachPresenter(listPresenter)
        listPresenter.loadTaskItems(if (intent.hasExtra(CHECK_ITEM_LIST)) intent.getSerializableExtra(CHECK_ITEM_LIST) as ArrayList<CheckItem> else null)
        deleteToggleIcon.setOnClickListener({
            //set state on adapter so all futures render out
            //get all visible item vh and translate
            if(checkListAdapter.itemList?.size == 0) return@setOnClickListener

            //state of switch can be derived from view switcher
            var firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
            var lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
            Timber.d("lastVisiblePosition $lastVisiblePosition")

            for(i in firstVisiblePosition .. lastVisiblePosition) {
                Timber.d("Position $i")

                //i can be used as a scalar for posting the animation
                var viewHolder = checkListView.findViewHolderForAdapterPosition(i)
                if(viewHolder is PendingItemViewHolder) {
                    viewHolder.taskForeground.translationX = SwipeActionListener.DELETE_TOGGLE_TRANSLATE_X
                    viewHolder.actionSwitch.displayedChild = PendingItemViewHolder.DELETE_VIEW
                    viewHolder.viewType = HolderType.DELETE_TOGGLE
                    var pendingItem = checkListAdapter.itemList[i] as PendingCheckItemModel
                    pendingItem.isDeleteToggled = true
                }
            }
//            checkListView.findViewHolderForAdapterPosition()
        })
    }

    override fun onPause() {
        super.onPause()
        intent.putExtra(CHECK_ITEM_LIST, checkListAdapter.itemList)
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        val toggleCurrentItems = menu.findItem(R.id.toggle_completed_items);
        toggleCurrentItems.setOnMenuItemClickListener {
            item -> if(item.isChecked) {
                item.isChecked = false
                checkListAdapter.hideCompletedItems()
            } else {
                item.isChecked = true
                checkListAdapter.showCompletedItems()
            }
            true
        }
        return true
    }

    fun initAdapterWith(itemList: ArrayList<CheckItem>) {
        checkListAdapter = ChecklistAdapter(itemList, listPresenter, this)
        swipeActionListener = SwipeActionListener(this,checkListAdapter)

        checkListView.setHasFixedSize(true)
        checkListView.adapter = checkListAdapter
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = false
        checkListView.layoutManager = linearLayoutManager

        checkListView.addOnItemTouchListener(swipeActionListener)
    }

    fun delegateHideDropShadow() {
        enterItemWidget?.hideDropShadow()
    }

    fun setScrollBehaviourWith() {
        //set rather than add is safer as prevents adding un-needed listeners
        checkListView.setOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if(lastVisibleItem == totalItemCount - 1) { //TODO Will need tweaking when show/hiding completed
                    enterItemWidget?.hideDropShadow()
                } else {
                    enterItemWidget?.showDropShadow()
                }
            }
        })
    }

    fun getLastCompletedItemPosition() : Int {
        return checkListAdapter.getLastCompletedItemPosition()
    }

    fun addPendingItemToAdapterWith(task: PendingCheckItemModel, position: Int) {
        checkListAdapter?.addItem(task, position)
        checkListView.scrollToPosition(getLastCompletedItemPosition() - 1)
    }

    fun showSnackbar() {
        Snackbar.make(rootView,
                      resources.getString(R.string.checklist_text),
                      Snackbar.LENGTH_LONG)
                          .setAction(resources.getString(R.string.checklist_action), {
                              val checkItemHolder = intent.getSerializableExtra(MOST_RECENTLY_REMOVED_MODEL) as CheckItemHolder
                              checkListAdapter.restoreItemWith(checkItemHolder?.position,
                                                                checkItemHolder?.pendingCheckItemModel,
                                                                checkItemHolder.completedCheckItemModel)
                          })
                      .setActionTextColor(resources.getColor(R.color.green))
                      .setCallback(object: Snackbar.Callback() {
                            override fun onDismissed(snackbar: Snackbar, event: Int) {
                                checkListAdapter.allowViewholderTypeTransform(true)
                            }

                            override  fun onShown(snackbar: Snackbar) {
                                checkListAdapter.allowViewholderTypeTransform(false)
                              }
                      })
                      .show()
    }

    fun storeIntentFor(taskItemHolder: CheckItemHolder) {
        intent.putExtra(MOST_RECENTLY_REMOVED_MODEL, taskItemHolder)
    }

    override fun finish() {
        super.finish()
        checkListView.removeOnItemTouchListener(swipeActionListener)
    }
}
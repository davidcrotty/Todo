package com.david.todo.view.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import butterknife.bindView
import com.david.todo.R
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.HolderType
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import com.david.todo.model.CheckItem
import com.david.todo.model.PendingToCompleteItemHolder
import com.david.todo.model.PendingCheckItemModel
import com.david.todo.model.PendingToDeleteCheckItemModel
import com.david.todo.presenter.TaskListPresenter
import com.david.todo.view.BaseActivity
import com.david.todo.view.eventlisteners.ListDragHandler
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
    val deleteToggleIconContainer: RelativeLayout by bindView(R.id.delete_action_icon_container)
    val deleteToggleViewSwitch: ViewSwitcher by bindView(R.id.delete_action_icon_switch)

    val DONE_DELETE_ICON_VIEW: Int = 1
    val TRASH_ICON_VIEW: Int = 0
    val MOST_RECENTLY_COMPLETED_MODEL: String = "MOST_RECENTLY_COMPLETED_MODEL"
    val MOST_RECENTLY_DELETED_MODEL: String = "MOST_RECENTLY_DELETED_MODEL"
    val CHECK_ITEM_LIST: String = "CHECK_ITEM_LIST"
    val COMPLETED_ITEMS_SHOULD_BE_SHOWN: String = "COMPLETED_ITEMS_SHOULD_BE_SHOWN"

    lateinit var swipeActionListener: SwipeActionListener
    lateinit var listPresenter: TaskListPresenter
    lateinit var checkListAdapter: ChecklistAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var deleteToggleMargin: Float? = null
    var itemTouchHelper: ItemTouchHelper? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(toolbar)
        deleteToggleMargin = -resources.getDimension(R.dimen.delete_text_hit_area)

        listPresenter = TaskListPresenter(this)
        enterItemWidget.attachPresenter(listPresenter)
        listPresenter.loadTaskItems(if (intent.hasExtra(CHECK_ITEM_LIST)) intent.getSerializableExtra(CHECK_ITEM_LIST) as ArrayList<CheckItem> else null)
        deleteToggleIconContainer.setOnClickListener({
            //TODO hide visibility when no items

            if(deleteToggleViewSwitch.displayedChild == TRASH_ICON_VIEW) {
                deleteToggleViewSwitch.displayedChild = DONE_DELETE_ICON_VIEW
                toggleItemsToViewStateWith(HolderType.DELETE_TOGGLE)
            } else {
                deleteToggleViewSwitch.displayedChild = TRASH_ICON_VIEW
                toggleItemsToViewStateWith(HolderType.PENDING)
            }
        })
    }

    /**
     * Changes state of view with either @HolderType.PENDING or @HolderType.DELETE_TOGGLE
     */
    fun toggleItemsToViewStateWith(viewType : HolderType) {
        if(checkListAdapter.itemList?.size == 0) return

        var firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
        var lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition()
//        Timber.d("lastVisiblePosition $lastVisiblePosition")

        for(i in firstVisiblePosition .. lastVisiblePosition) {
//            Timber.d("Position $i")

            var viewHolder = checkListView.findViewHolderForAdapterPosition(i)
            if(viewHolder is PendingItemViewHolder) {
                if(viewType == HolderType.PENDING ) {
                    viewHolder.taskForeground.translationX = SwipeActionListener.PENDING_TRANSLATE_X
                    viewHolder.actionSwitch.displayedChild = PendingItemViewHolder.COMPLETE_VIEW
                    viewHolder.viewType = HolderType.PENDING
                    var pendingItem = checkListAdapter.itemList[i] as PendingCheckItemModel
                    pendingItem.isDeleteToggled = false
                } else if (viewType == HolderType.DELETE_TOGGLE) {
                    viewHolder.taskForeground.translationX = deleteToggleMargin!!
                    viewHolder.actionSwitch.displayedChild = PendingItemViewHolder.DELETE_VIEW
                    viewHolder.viewType = HolderType.DELETE_TOGGLE
                    var pendingItem = checkListAdapter.itemList[i] as PendingCheckItemModel
                    pendingItem.isDeleteToggled = true
                }

            }
        }
    }

    override fun onPause() {
        super.onPause()
        intent.putExtra(CHECK_ITEM_LIST, checkListAdapter.itemList)
    }

    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.menu_main, menu);
        val toggleCurrentItems = menu.findItem(R.id.toggle_completed_items);

        if(intent.hasExtra(COMPLETED_ITEMS_SHOULD_BE_SHOWN)) {
            if(intent.getBooleanExtra(COMPLETED_ITEMS_SHOULD_BE_SHOWN, false)) {
                toggleCurrentItems.isChecked = true
                checkListAdapter.showCompletedItems()
            } else {
                toggleCurrentItems.isChecked = false
                checkListAdapter.hideCompletedItems()
            }
        } else {
            checkListAdapter.hideCompletedItems()
        }

        toggleCurrentItems.setOnMenuItemClickListener {
            item -> if(item.isChecked) {
                item.isChecked = false
                intent.putExtra(COMPLETED_ITEMS_SHOULD_BE_SHOWN, false)
                checkListAdapter.hideCompletedItems()
            } else {
                item.isChecked = true
                intent.putExtra(COMPLETED_ITEMS_SHOULD_BE_SHOWN, true)
                checkListAdapter.showCompletedItems()
            }
            true
        }
        return true
    }

    override fun finish() {
        super.finish()
        checkListView.removeOnItemTouchListener(swipeActionListener)
    }
    

    fun initAdapterWith(itemList: ArrayList<CheckItem>) {
        checkListAdapter = ChecklistAdapter(itemList, listPresenter, this, deleteToggleMargin!!)
        swipeActionListener = SwipeActionListener(this,checkListAdapter, deleteToggleMargin!!)

        checkListView.setHasFixedSize(true)
        checkListView.adapter = checkListAdapter
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = false
        checkListView.layoutManager = linearLayoutManager

        checkListView.addOnItemTouchListener(swipeActionListener)

        var dragHelper = ListDragHandler(checkListAdapter)
        itemTouchHelper = ItemTouchHelper(dragHelper)
        itemTouchHelper?.attachToRecyclerView(checkListView)
    }

    fun delegateHideDropShadow() {
        enterItemWidget.hideDropShadow()
    }

    fun setScrollBehaviourWith() {
        //set rather than add is safer as prevents adding un-needed listeners
        checkListView.setOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                if(lastVisibleItem == totalItemCount - 1) { //TODO Will need tweaking when show/hiding completed
                    enterItemWidget.hideDropShadow()
                } else {
                    enterItemWidget.showDropShadow()
                }
            }
        })
    }

    fun getLastCompletedItemPosition() : Int {
        return checkListAdapter.getLastCompletedItemPosition()
    }

    fun addPendingItemToAdapterWith(task: PendingCheckItemModel, position: Int) {
        checkListAdapter.addItem(task, position)
        checkListView.scrollToPosition(getLastCompletedItemPosition() - 1)
    }

    fun showSnackbarWith(text: String, undoColourId: Int, action: View.OnClickListener) {
        Snackbar.make(rootView,
                      text,
                      Snackbar.LENGTH_LONG)
                          .setAction(resources.getString(R.string.checklist_action), action)
                      .setActionTextColor(undoColourId)
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

    fun storeIntentFor(taskItemHolder: PendingToCompleteItemHolder) {
        intent.putExtra(MOST_RECENTLY_COMPLETED_MODEL, taskItemHolder)
    }

    fun undoCompletedItem() {
        val checkItemHolder = intent.getSerializableExtra(MOST_RECENTLY_COMPLETED_MODEL) as PendingToCompleteItemHolder
        checkListAdapter.restoreCompletedItemWith(checkItemHolder.position,
                checkItemHolder.pendingCheckItemModel,
                checkItemHolder.completedCheckItemModel)
    }

    fun undoDeletedItem() {
        var deletedItem: PendingToDeleteCheckItemModel = intent.getSerializableExtra(MOST_RECENTLY_DELETED_MODEL) as PendingToDeleteCheckItemModel ?: return
        var childStateChangedListener = object : RecyclerView.OnChildAttachStateChangeListener{
            override fun onChildViewDetachedFromWindow(view: View?) {

            }

            override fun onChildViewAttachedToWindow(view: View?) {
                var pendingViewHolder = linearLayoutManager.findViewByPosition(deletedItem.position) ?: return
                //go find it from a view
                var viewHolder = checkListView.getChildViewHolder(pendingViewHolder)
                if(viewHolder is PendingItemViewHolder) {
                    viewHolder.taskForeground.translationX = 0F
                    var model = checkListAdapter.getModelFor(deletedItem.position) as PendingCheckItemModel
                    model?.isDeleteToggled = false
                }
                checkListView.removeOnChildAttachStateChangeListener(this)
            }

        }

        checkListView.addOnChildAttachStateChangeListener(childStateChangedListener)
        checkListAdapter.restorePendingCheckItemWith(deletedItem.position, deletedItem.pendingToDeleteCheckItemModel)
    }

    fun pendingItemDeleted(pendingItemViewHolder: PendingItemViewHolder) {
        val position = pendingItemViewHolder.adapterPosition;
        if(position == RecyclerView.NO_POSITION) return
        var item = checkListAdapter.getModelFor(position)
        if(item is PendingCheckItemModel) {
            intent.putExtra(MOST_RECENTLY_DELETED_MODEL, PendingToDeleteCheckItemModel(item, position))
            checkListAdapter.deleteItem(position)
            showSnackbarWith(resources.getString(R.string.checklist_delete_text),
                             resources.getColor(R.color.red),
                             View.OnClickListener {
                                 undoDeletedItem()
                             })
        }
    }

    fun startListItemDragWith(pendingItemViewHolder: PendingItemViewHolder) {
        swipeActionListener.reset()
        itemTouchHelper?.startDrag(pendingItemViewHolder)
    }
}
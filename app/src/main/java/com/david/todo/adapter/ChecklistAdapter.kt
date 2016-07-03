package com.david.todo.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.david.todo.R
import com.david.todo.adapter.viewholder.CompletedItemViewHolder
import com.david.todo.adapter.viewholder.HolderType
import com.david.todo.adapter.viewholder.PendingItemViewHolder
import com.david.todo.model.CheckItem
import com.david.todo.model.CompletedCheckItemModel
import com.david.todo.model.PendingCheckItemModel
import com.david.todo.model.TaskItemModel
import com.david.todo.presenter.TaskListPresenter
import com.david.todo.view.eventlisteners.SwipeActionListener
import com.david.todo.view.widgets.TabView
import timber.log.Timber
import java.util.*

/**
 * Created by DavidHome on 03/04/2016.
 */
class ChecklistAdapter(val itemList: ArrayList<CheckItem>,
                       val listPresenter: TaskListPresenter,
                       val context: Context,
                       val deleteToggleMargin: Float) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val defaultPositionX: Float = 0F
    var _allowTransform: Boolean = true
    var drawCompletedItems: Boolean = false

    override fun getItemViewType(position: Int): Int {
        var checkItem = itemList[position]
        Timber.d("getItemViewType")

        if(checkItem is PendingCheckItemModel) {
            if(checkItem.isDeleteToggled) {
                Timber.d("isDeleteToggled $position")
                return HolderType.DELETE_TOGGLE.ordinal
            } else {
                Timber.d("pending $position")
                return HolderType.PENDING.ordinal
            }
        } else {
            return HolderType.COMPLETED.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent?.context)
        return when(viewType) {
            HolderType.PENDING.ordinal -> {
                val parentLayout = inflater.inflate(R.layout.pending_check_list_item, parent, false)
                return PendingItemViewHolder(parentLayout)
            }
            HolderType.COMPLETED.ordinal -> {
                val parentLayout = inflater.inflate(R.layout.completed_check_list_item, parent, false)
                return CompletedItemViewHolder(parentLayout)
            }
            HolderType.DELETE_TOGGLE.ordinal -> {
                val parentLayout = inflater.inflate(R.layout.pending_check_list_item, parent, false)
                return PendingItemViewHolder(parentLayout)
            }
            else -> {
                return null
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(holder?.itemViewType) {
            HolderType.PENDING.ordinal -> {
                holder as PendingItemViewHolder
                val pendingItemModel = itemList[position] as PendingCheckItemModel
                holder?.taskText?.text = pendingItemModel.text
                holder?.actionSwitch?.displayedChild = PendingItemViewHolder.COMPLETE_VIEW
                holder?.taskForeground?.translationX = defaultPositionX
                holder?.itemView?.visibility = View.VISIBLE

                holder?.taskForeground?.isLongClickable = true
                holder?.taskForeground?.visibility = View.VISIBLE
            }

            HolderType.COMPLETED.ordinal -> {
                holder as CompletedItemViewHolder
                if(drawCompletedItems) {
                    holder?.completedItemContainer?.visibility = View.VISIBLE
                    val completedItemModel = itemList[position] as CompletedCheckItemModel
                    holder?.taskText?.text = completedItemModel.text
                    holder?.undoButtonText?.setOnClickListener({
                        if (_allowTransform == false) return@setOnClickListener
                        replaceCompletedWithPendingItem(completedItemModel)
                    })
                } else {
                    holder?.completedItemContainer?.visibility = View.GONE
                }
            }

            HolderType.DELETE_TOGGLE.ordinal -> {
                holder as PendingItemViewHolder
                val pendingItemModel = itemList[position] as PendingCheckItemModel
                holder?.taskText?.text = pendingItemModel.text
                holder?.actionSwitch?.displayedChild = PendingItemViewHolder.DELETE_VIEW
                holder?.taskForeground?.translationX = deleteToggleMargin
                holder?.itemView?.visibility = View.VISIBLE

                holder?.taskForeground?.isLongClickable = true
                holder?.taskForeground?.visibility = View.VISIBLE
            }
        }
    }

    fun replaceCompletedWithPendingItem(completedCheckItemModel: CompletedCheckItemModel) {
        for(i in itemList.indices) {
            if(itemList[i] is CompletedCheckItemModel) {
                itemList.remove(completedCheckItemModel)
                itemList.add(i, PendingCheckItemModel(completedCheckItemModel.text))
                notifyDataSetChanged()
                return
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count();
    }

    fun hideCompletedItems() {
        drawCompletedItems = false
        notifyDataSetChanged()
    }

    fun showCompletedItems() {
        drawCompletedItems = true
        notifyDataSetChanged()
    }

    fun addItem(task: PendingCheckItemModel, position: Int) {
        itemList.add(position, task)
        notifyItemInserted(position)
    }

    fun getLastCompletedItemPosition() : Int {
        for(i in itemList.indices) {
            if(itemList[i] is CompletedCheckItemModel) {
                return if(i == 0) 0 else i
            }
        }
        return itemList.size
    }

    fun onItemDismiss(position: Int) {
        Timber.d("Removing $position")
        val pendingTaskItem = itemList[position] as PendingCheckItemModel
        val completedItem = CompletedCheckItemModel(pendingTaskItem.text)
        itemList.removeAt(position)
        notifyItemRemoved(position)
        itemList.add(completedItem)
        notifyItemInserted(itemList.size - 1)
        listPresenter.storeAndDisplaySnackBarFor(pendingTaskItem, completedItem, position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean{
        if (fromPosition < toPosition) {
            for(i in fromPosition..toPosition - 1) {
                Collections.swap(itemList, i, i + 1);
            }
        } else {
            for(i in fromPosition downTo toPosition + 1) {
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    fun restoreItemWith(savedPosition: Int, checkItemToAdd: PendingCheckItemModel, completedItemToRemove: CompletedCheckItemModel?) {
        itemList.add(savedPosition, checkItemToAdd)
        notifyItemInserted(savedPosition)
        completedItemToRemove?.let {
            itemList.remove(completedItemToRemove as CheckItem)
            notifyItemRemoved(itemList.size)
        }
    }

    fun allowViewholderTypeTransform(allowTransform: Boolean) {
        _allowTransform = allowTransform
    }
}
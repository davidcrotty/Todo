package com.david.todo.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
import com.david.todo.view.eventlisteners.IHandleListener
import com.david.todo.view.widgets.TabView
import timber.log.Timber
import java.util.*

/**
 * Created by DavidHome on 03/04/2016.
 */
class ChecklistAdapter(val itemList: ArrayList<CheckItem>,
                       val listPresenter: TaskListPresenter,
                       val context: Context,
                       val dragListener: IHandleListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val defaultPositionX: Float = 0F
    var _allowTransform: Boolean = true

    override fun getItemViewType(position: Int): Int {
        if(itemList[position] is PendingCheckItemModel) {
            return HolderType.PENDING.ordinal
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
                holder?.dragHandle?.setOnTouchListener({ view, motionEvent ->
                    when(motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            dragListener.onHandleDown(holder)
                            true
                        }
                        else -> false
                    }
                });

                holder?.taskForeground?.translationX = defaultPositionX
                holder?.itemView?.visibility = View.VISIBLE
            }
            HolderType.COMPLETED.ordinal -> {
                holder as CompletedItemViewHolder
                val completedItemModel = itemList[position] as CompletedCheckItemModel
                holder?.taskText?.text = completedItemModel.text
                holder?.undoButtonText?.setOnClickListener({
                    if(_allowTransform == false) return@setOnClickListener
                    replaceCompletedWithPendingItem(completedItemModel,
                                                    position)
                })
            }
        }
    }

    fun replaceCompletedWithPendingItem(completedCheckItemModel: CompletedCheckItemModel,
                                        position: Int) {
        //when toast is displating disable all undo buttons
        for(i in itemList.indices) {
            if(itemList[i] is CompletedCheckItemModel && i != 0) {
                itemList.remove(completedCheckItemModel)
                notifyItemRemoved(position)
                itemList.add(i, PendingCheckItemModel(completedCheckItemModel.text))
                notifyItemInserted(i)
                return
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count();
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
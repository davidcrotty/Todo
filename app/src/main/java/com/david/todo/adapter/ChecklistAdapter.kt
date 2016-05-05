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
import com.david.todo.model.CheckItemModel
import com.david.todo.model.TaskItemModel
import com.david.todo.presenter.TaskListPresenter
import com.david.todo.view.eventlisteners.IHandleListener
import com.david.todo.view.widgets.TabView
import timber.log.Timber
import java.util.*

/**
 * Created by DavidHome on 03/04/2016.
 */
class ChecklistAdapter(val itemList: ArrayList<CheckItemModel>,
                       val listPresenter: TaskListPresenter,
                       val context: Context,
                       val dragListener: IHandleListener) : RecyclerView.Adapter<ChecklistAdapter.ItemViewHolder>() {

    val defaultPositionX: Float = 0F

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        var layout = LayoutInflater.from(parent?.context).inflate(R.layout.check_list_item, parent, false)
        val viewHolder = ItemViewHolder(layout);
        return viewHolder;
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        holder?.textView?.text = itemList[position].text
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

    override fun getItemCount(): Int {
        return itemList.count();
    }

    fun onItemDismiss(position: Int) {
        Timber.d("Removing $position")
        val taskItem = itemList[position]
        itemList.removeAt(position)
        notifyItemRemoved(position)
        listPresenter.storeAndDisplaySnackBarFor(taskItem, position)
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

    fun restoreItemWith(savedPosition: Int, itemToAdd: CheckItemModel) {
        itemList.add(savedPosition, itemToAdd)
        notifyItemInserted(savedPosition)
//        notifyDataSetChanged()
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView
        var dragHandle: ImageView
        var doneImage: ImageView
        var taskForeground: FrameLayout
        var taskBackground: FrameLayout

        init {
            textView = view.findViewById(R.id.text_item) as TextView
            dragHandle = view.findViewById(R.id.drag_handle) as ImageView
            doneImage = view.findViewById(R.id.done_image) as ImageView
            doneImage.setColorFilter(view.context.getColor(R.color.green_ripple))
            taskForeground = view.findViewById(R.id.task_foreground) as FrameLayout
            taskBackground = view.findViewById(R.id.task_background) as FrameLayout
        }
    }
}
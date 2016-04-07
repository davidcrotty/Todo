package com.david.todo.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.david.todo.R
import com.david.todo.model.CheckItemModel
import java.util.*

/**
 * Created by DavidHome on 03/04/2016.
 */
class ChecklistAdapter(val itemList: ArrayList<CheckItemModel>) : RecyclerView.Adapter<ChecklistAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        var layout = LayoutInflater.from(parent?.context).inflate(R.layout.check_list_item, parent, false)
        val viewHolder = ItemViewHolder(layout);
        return viewHolder;
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        holder?.textView?.text = itemList[position].text
        holder?.handle?.setBackgroundColor(itemList[position].colour)
    }

    override fun getItemCount(): Int {
        return itemList.count();
    }

    fun onItemDismiss(position: Int) {
        itemList.removeAt(position);
        notifyItemRemoved(position);
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


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textView: TextView
        var handle: View

        init {
            textView = view.findViewById(R.id.text_item) as TextView
            handle = view.findViewById(R.id.drag_handle) as View
        }
    }
}
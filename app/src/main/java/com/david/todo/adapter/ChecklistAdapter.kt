package com.david.todo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.david.todo.R
import java.util.*

/**
 * Created by DavidHome on 03/04/2016.
 */
class ChecklistAdapter(val itemList: ArrayList<String>) : RecyclerView.Adapter<ChecklistAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder? {
        var layout = LayoutInflater.from(parent?.context).inflate(R.layout.check_list_item, parent, false)
        val viewHolder = ItemViewHolder(layout);
        return viewHolder;
    }

    override fun onBindViewHolder(holder: ItemViewHolder?, position: Int) {
        holder?.textView?.text = itemList[position];
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
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mItems, i, i + 1);
//            }
        } else {
            for(i in fromPosition downTo toPosition + 1) {
                Collections.swap(itemList, i, i - 1);
            }
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mItems, i, i - 1);
//            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var textView: TextView

        init {
            textView = view.findViewById(R.id.text_item) as TextView
        }
    }
}
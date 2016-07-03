package com.david.todo.view.eventlisteners

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.david.todo.adapter.ChecklistAdapter
import com.david.todo.adapter.viewholder.PendingItemViewHolder

/**
 * Created by DavidHome on 18/04/2016.
 */
class FadeAnimation(val fromAlpha: Float, val toAlpha: Float, val viewHolder: RecyclerView.ViewHolder, val checkListAdapter: ChecklistAdapter) : AlphaAnimation(fromAlpha, toAlpha), Animation.AnimationListener {
    lateinit var parentView: View

    init {
        val holder = viewHolder as PendingItemViewHolder
        parentView = holder.itemView
        setAnimationListener(this)
    }

    fun startAnimation() {
        parentView.startAnimation(this)
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
        parentView.visibility = View.INVISIBLE
        //TODO if becoming a library this will need a removal strategy to be used in recyclerViews
        //TODO this event should be delegated to the Activity to handle the event not the adapter
        checkListAdapter.dismissAndReplaceWithCompletedItem(viewHolder!!.adapterPosition);
    }

    override fun onAnimationStart(animation: Animation?) {
    }
}
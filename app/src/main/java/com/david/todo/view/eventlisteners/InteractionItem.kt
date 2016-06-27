package com.david.todo.view.eventlisteners

import android.view.ViewGroup
import android.widget.ViewSwitcher
import com.david.todo.adapter.viewholder.HolderType
import com.david.todo.adapter.viewholder.PendingItemViewHolder

/**
 * Created by DavidHome on 21/06/2016.
 *
 * All of these items can be taken from the viewholder?
 */
data class InteractionItem(val foreground: ViewGroup,
                           val background: ViewSwitcher,
                           val pendingItemViewHolder: PendingItemViewHolder,
                           val foregroundViewPosition: Float,
                           var actionViewType: HolderType,
                           val interactionStartPosition: Float)
package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 19/04/2016.
 */
data class CheckItemHolder(val pendingItemModel: PendingItemModel, val position: Int) : Serializable
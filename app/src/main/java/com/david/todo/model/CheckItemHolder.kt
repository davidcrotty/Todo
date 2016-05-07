package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 19/04/2016.
 */
data class CheckItemHolder(val pendingCheckItemModel: PendingCheckItemModel, val position: Int) : Serializable
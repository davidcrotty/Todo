package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 19/04/2016.
 */
data class PendingToCompleteItemHolder(val pendingCheckItemModel: PendingCheckItemModel,
                                       val completedCheckItemModel: CompletedCheckItemModel,
                                       val position: Int) : Serializable
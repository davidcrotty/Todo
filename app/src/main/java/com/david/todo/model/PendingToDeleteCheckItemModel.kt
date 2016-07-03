package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 03/07/2016.
 */
data class PendingToDeleteCheckItemModel(val pendingToDeleteCheckItemModel: PendingCheckItemModel,
                                         val position: Int) : Serializable
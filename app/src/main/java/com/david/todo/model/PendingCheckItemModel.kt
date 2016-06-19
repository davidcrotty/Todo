package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 07/04/2016.
 */
data class PendingCheckItemModel(val text:String, var isDeleteToggled: Boolean = false) : Serializable, CheckItem
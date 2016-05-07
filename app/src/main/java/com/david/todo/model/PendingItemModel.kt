package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 07/04/2016.
 */
data class PendingItemModel(val text:String) : Serializable, CheckItem
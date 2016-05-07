package com.david.todo.model

import java.io.Serializable

/**
 * Created by DavidHome on 07/05/2016.
 */
data class CompletedCheckItemModel(val text:String) : Serializable, CheckItem
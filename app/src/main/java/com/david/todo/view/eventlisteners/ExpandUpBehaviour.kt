package com.david.todo.view.eventlisteners

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View

/**
 * Created by DavidHome on 15/05/2016.
 */
class ExpandUpBehaviour(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun onDependentViewChanged(parent: CoordinatorLayout , child: View, dependency: View) : Boolean {
        val translationY = Math.min(0, dependency.translationY.toInt() - dependency.height);
        child.translationY = translationY.toFloat();
        return true;
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout;
    }
}
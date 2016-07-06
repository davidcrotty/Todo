package com.david.todo.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.david.todo.R
import kotlinx.android.synthetic.main.activity_toolbar.*

class ToolbarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)

        setSupportActionBar(toolbar)

        action_mode_button.setOnClickListener({
            startSupportActionMode(object: android.support.v7.view.ActionMode.Callback {
                override fun onPrepareActionMode(mode: android.support.v7.view.ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: android.support.v7.view.ActionMode?, item: MenuItem?): Boolean {
                    return false
                }

                override fun onCreateActionMode(mode: android.support.v7.view.ActionMode?, menu: Menu?): Boolean {
                    var inflater = mode!!.menuInflater
                    inflater.inflate(R.menu.edit_text_menu, menu)
                    return true
                }

                override fun onDestroyActionMode(mode: android.support.v7.view.ActionMode?) {
                }
            })

        })
    }
}

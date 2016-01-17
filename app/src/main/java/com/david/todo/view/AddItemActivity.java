package com.david.todo.view;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;

import com.david.todo.R;
import com.david.todo.adapter.TodoItemListAdapter;
import com.david.todo.model.AnimateLocationCoordinatesModel;
import com.david.todo.model.TaskItemModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by DavidHome on 10/01/2016.
 */
public class AddItemActivity extends BaseActivity {

    public static String ANIMATE_START_INTENT_KEY = "ANIMATE_START_INTENT_KEY";

    @Bind(R.id.add_item_root)
    CoordinatorLayout _rootView;

    @Bind(R.id.add_option_list)
    RecyclerView _addOptionList;

    @Bind(R.id.toolbar)
    Toolbar _toolbar;

    @Bind(R.id.collapsing_container)
    CollapsingToolbarLayout _collapsingToolbar;

    private SupportAnimator _circularReveal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_full_view);
        ButterKnife.bind(this);
//        setSupportActionBar(_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _addOptionList.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<TaskItemModel> taskItemModelList = new ArrayList();
        TaskItemModel a = new TaskItemModel("Description", "Enter description");
        TaskItemModel b = new TaskItemModel("Add Task", "...");
        TaskItemModel c = new TaskItemModel("Date", "Set date");
        TaskItemModel d = new TaskItemModel("Description", "Enter description");
        TaskItemModel e = new TaskItemModel("Add Task", "...");
        TaskItemModel f = new TaskItemModel("Date", "Set date");
        TaskItemModel g = new TaskItemModel("Description", "Enter description");
        TaskItemModel h = new TaskItemModel("Add Task", "...");
        TaskItemModel i = new TaskItemModel("Date", "Set date");

        taskItemModelList.add(a);
        taskItemModelList.add(b);
        taskItemModelList.add(c);
        taskItemModelList.add(d);
        taskItemModelList.add(e);
        taskItemModelList.add(f);
        taskItemModelList.add(g);
        taskItemModelList.add(h);
        taskItemModelList.add(i);

        TodoItemListAdapter todoItemListAdapter = new TodoItemListAdapter(taskItemModelList);
        _addOptionList.setAdapter(todoItemListAdapter);

        _rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                circularRevealLayout();
            }
        });
    }

    private void circularRevealLayout() {
        if(getIntent() == null) return;
        if(getIntent().hasExtra(ANIMATE_START_INTENT_KEY) == false) {
            _rootView.setVisibility(View.VISIBLE);
            return;
        }

        AnimateLocationCoordinatesModel animateModel = (AnimateLocationCoordinatesModel) getIntent()
                                                                                         .getExtras()
                                                                                         .getSerializable(ANIMATE_START_INTENT_KEY);

        int cx = animateModel.getX() + animateModel.getWidth()  / 2;
        int cy = animateModel.getY() + animateModel.getHeight() / 2;

        float finalRadius = Math.max(_rootView.getWidth(), _rootView.getHeight());

        _circularReveal = ViewAnimationUtils.createCircularReveal(_rootView, cx, cy, 0, finalRadius);
        _circularReveal.setDuration(500);

        _rootView.setVisibility(View.VISIBLE);
        _circularReveal.start();
        _circularReveal.reverse();
        getIntent().removeExtra(ANIMATE_START_INTENT_KEY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(_circularReveal != null) {
            _circularReveal.reverse();
        }
    }
}

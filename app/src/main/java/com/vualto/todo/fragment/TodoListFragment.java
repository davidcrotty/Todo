package com.vualto.todo.fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.vualto.todo.R;
import com.vualto.todo.module.component.DaggerTodoListFragmentPresenterComponent;
import com.vualto.todo.module.module.TodoListFragmentPresenterModule;
import com.vualto.todo.presenter.TodoListFragmentPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by David on 18/09/2015.
 */
public class TodoListFragment extends Fragment implements View.OnLayoutChangeListener {

    @Bind(R.id.add_item_button)
    FloatingActionButton _addItemButton;

    @Inject
    TodoListFragmentPresenter _todoTodoListFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        DaggerTodoListFragmentPresenterComponent.builder()
                .todoListFragmentPresenterModule(new TodoListFragmentPresenterModule(this))
                .build()
                .inject(this);
        view.addOnLayoutChangeListener(this);
        return view;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int finalRadius = Math.max(_addItemButton.getWidth(), _addItemButton.getHeight());
        int cx = _addItemButton.getWidth() / 2;
        int cy = _addItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_addItemButton, cx, cy, 0, finalRadius);
        anim.setDuration(1000);
        anim.start();
    }
}

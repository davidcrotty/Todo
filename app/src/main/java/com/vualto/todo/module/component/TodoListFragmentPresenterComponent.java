package com.vualto.todo.module.component;

import com.vualto.todo.fragment.TodoListFragment;
import com.vualto.todo.module.module.TodoListFragmentPresenterModule;
import com.vualto.todo.presenter.TodoListFragmentPresenter;

import dagger.Component;

/**
 * Created by David on 18/09/2015.
 */
@Component(modules = TodoListFragmentPresenterModule.class)
public interface TodoListFragmentPresenterComponent {
    TodoListFragmentPresenter providePresenter();
    void inject(TodoListFragment fragment);
}

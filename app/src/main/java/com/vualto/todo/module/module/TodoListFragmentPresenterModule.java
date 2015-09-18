package com.vualto.todo.module.module;

import com.vualto.todo.fragment.TodoListFragment;
import com.vualto.todo.presenter.TodoListFragmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by David on 18/09/2015.
 */
@Module
public class TodoListFragmentPresenterModule {
    private final TodoListFragment _fragment;
    public TodoListFragmentPresenterModule(TodoListFragment fragment){
        _fragment = fragment;
    }

    @Provides
    public TodoListFragmentPresenter providePresenter() {
        return new TodoListFragmentPresenter(_fragment);
    }
}

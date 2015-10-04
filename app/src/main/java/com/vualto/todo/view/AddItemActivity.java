package com.vualto.todo.view;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;

import com.vualto.todo.R;
import com.vualto.todo.appstart.AndroidApplication;
import com.vualto.todo.module.DaggerDataRepositoryComponent;
import com.vualto.todo.module.DaggerTodoListPresenterComponent;
import com.vualto.todo.module.DataRepositoryComponent;
import com.vualto.todo.module.DataRepositoryModule;
import com.vualto.todo.module.TodoListPresenterModule;
import com.vualto.todo.presenter.TodoListPresenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;

/**
 * Created by David on 20/09/2015.
 */
public class AddItemActivity extends BaseActivity implements View.OnLayoutChangeListener,
                                                             Animator.AnimatorListener,
                                                             TodoView {

    @Bind(R.id.notes_edit_text)
    EditText _notesText;
    @Bind(R.id.notes_wrapper)
    TextInputLayout _notesTextWrapper;
    @Bind(R.id.save_item_button)
    FloatingActionButton _saveItemButton;

    private DataRepositoryComponent _dataRepositoryComponent;

    @Inject
    Lazy<TodoListPresenter> _presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        initialiseInjector();
        _saveItemButton.addOnLayoutChangeListener(this);
        _notesTextWrapper.setHint("Tap Below to add notes");
    }

    private void initialiseInjector() {
        _dataRepositoryComponent = DaggerDataRepositoryComponent.builder()
                .dataRepositoryModule(new DataRepositoryModule((AndroidApplication)getApplicationContext()))
                .build();

        DaggerTodoListPresenterComponent.builder()
                .dataRepositoryComponent(_dataRepositoryComponent)
                .todoListPresenterModule(new TodoListPresenterModule(this))
                .build().inject(this);
    }

    @OnClick(R.id.save_item_button)
    void onClick() {
        int startRadius = Math.max(_saveItemButton.getWidth(), _saveItemButton.getHeight());
        int cx = _saveItemButton.getWidth() / 2;
        int cy = _saveItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_saveItemButton, cx, cy, startRadius, 0);
        anim.addListener(this);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(700);
        anim.start();
        _presenter.get().addItem(_notesText.getText().toString());
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int finalRadius = Math.max(_saveItemButton.getWidth(), _saveItemButton.getHeight());
        int cx = _saveItemButton.getWidth() / 2;
        int cy = _saveItemButton.getHeight() / 2;

        Animator anim = ViewAnimationUtils.createCircularReveal(_saveItemButton, cx, cy, 0, finalRadius);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(700);
        anim.start();
    }

    @Override
    public void showTodoItems() {

    }
}

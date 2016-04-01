package com.david.todo.module;

import com.david.todo.presenter.AddItemPresenter;
import com.david.todo.service.EventService;

import dagger.Component;

/**
 * Created by DavidHome on 02/04/2016.
 */
@Component(modules = EventServiceModule.class)
public interface EventServiceComponent {
    EventService eventService();
    void inject(AddItemPresenter presenter);
}

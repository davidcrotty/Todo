package com.david.todo.module;

import com.david.todo.service.EventService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DavidHome on 02/04/2016.
 */
@Module
public class EventServiceModule {

    @Provides
    EventService provideEventService() {
        return new EventService();
    }
}

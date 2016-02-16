package com.david.todo.appstart;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.david.todo.BuildConfig;
import com.david.todo.R;
import com.david.todo.module.DataRepositoryComponent;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;

/**
 * Created by David on 29/09/2015.
 */
public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initialiseRealm();
    }

    private void initialiseRealm() {
        File realmDirectory = new File(Configuration.getRealmDirectory(this));
        if(realmDirectory.exists() == false) {
            realmDirectory.mkdirs();
        }
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(realmDirectory)
                .name(getResources()
                        .getString(R.string.app_name))
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded() //deletes all data each app install if schema changed.
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}

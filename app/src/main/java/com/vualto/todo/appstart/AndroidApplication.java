package com.vualto.todo.appstart;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.vualto.todo.BuildConfig;
import com.vualto.todo.R;
import com.vualto.todo.module.DataRepositoryComponent;

import java.io.File;
import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by David on 29/09/2015.
 */
public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
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
